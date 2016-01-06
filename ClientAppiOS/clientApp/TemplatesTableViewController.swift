import UIKit

protocol TemplatesControllerDelegate: class {
    func templatesController(templatesVC: TemplatesTableViewController)
}

class TemplatesTableViewController: PFQueryTableViewController {
    var transition: CustomTransition!
    
    let cellIdentifier = "TemplateCell"
    var userTemplates = [FormTemplate]()
    var ownersTemplates = [FormTemplate]()
    var myProfileId = ""
    var multipleForms = [String]()
    var userTemplatesCount = 0;
    var isZeroHeight = [Bool]()
    var rowMax = 0;
    var maxReached = false;
    
    override init(style: UITableViewStyle, className: String!) {
        
        super.init(style: style, className: className)
        
        self.pullToRefreshEnabled = true
        self.paginationEnabled = false
        self.objectsPerPage = 25
        
        self.parseClassName = className
    }
    
    required init(coder aDecoder:NSCoder) {
        fatalError("NSCoding not supported")
    }
    
    override func viewDidLoad() {
        self.navigationItem.title = "Forms"
        tableView.registerNib(UINib(nibName: "TemplateTableViewCell", bundle: nil), forCellReuseIdentifier: cellIdentifier)
        tableView.tableFooterView = UIView()
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    override func queryForTable() -> PFQuery {
        let predicate = NSPredicate(format:"user == 'edmondpr@gmail.com' OR user == nil")
        var query:PFQuery = PFQuery(className:self.parseClassName!, predicate: predicate)
        
        if (objects?.count == 0) {
            query.cachePolicy = PFCachePolicy.CacheThenNetwork
        }
        
        query.orderByDescending("user")
        query.addAscendingOrder("name")
        query.addAscendingOrder("owner")
        
        return query
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath, object: PFObject?) -> PFTableViewCell? {
        
        var cell:TemplateTableViewCell? = tableView.dequeueReusableCellWithIdentifier(cellIdentifier) as? TemplateTableViewCell
        
        if cell == nil {
            cell = NSBundle.mainBundle().loadNibNamed("TemplateTableViewCell", owner: self, options: nil)[0] as? TemplateTableViewCell
        }
        
        if indexPath.row > rowMax {
            rowMax = indexPath.row
        } else if indexPath.row < rowMax {
            maxReached = true
        }
        
        if let pfObject = object {
            var nameField:String? = pfObject["name"] as? String
            if nameField != nil {
                let userTemplateId:String? = pfObject.objectId
                let userTemplateName:String? = pfObject["name"] as? String
                cell?.templateName?.text = userTemplateName
                if !maxReached {
                    userTemplatesCount++
                    let userTemplateObj = FormTemplate(objectId: userTemplateId!, owner: "", type: "", name:userTemplateName!)
                    userTemplates.append(userTemplateObj)
                }
                if (nameField == "My Profile") {
                    myProfileId = pfObject.objectId!
                }
            } else {
                let ownerName:String? = pfObject["owner"] as? String
                let ownerType:String? = pfObject["type"] as? String
                let ownerId:String? = pfObject.objectId
                var exists = false
                for ownerObj in ownersTemplates {
                    if ownerObj.owner == ownerName {
                        exists = true
                        break
                    }
                }
                if !exists {
                    cell?.templateName?.text = ownerName
                    multipleForms.append(ownerId!)
                } else {
                    if !maxReached {
                        isZeroHeight[indexPath.row] = true
                        var i = 1;
                        while multipleForms[multipleForms.endIndex - i] == "" {
                            i++
                        }
                        multipleForms[multipleForms.endIndex - i] += "," + ownerId!
                        multipleForms.append("")
                    }
                }
                if ownerId != nil && !maxReached {
                    let ownerObj = FormTemplate(objectId: ownerId!, owner: ownerName!, type: ownerType!, name:"")
                    ownersTemplates.append(ownerObj)
                }
            }
        }
        
        return cell
        
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        var height:CGFloat = 50;
        if isZeroHeight.ref(indexPath.row) == nil {
            isZeroHeight.append(false);
        }
        if isZeroHeight[indexPath.row] {
            height = 0
        }
        return height;
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if indexPath.row < userTemplatesCount {
            var formTableVC:FormTableViewController = FormTableViewController(className: "ClientsFields")
            formTableVC.formId = userTemplates[indexPath.row].objectId
            formTableVC.myProfileId = self.myProfileId
            self.navigationController?.pushViewController(formTableVC, animated: true)
        } else if indexPath.row >= userTemplatesCount && multipleForms[indexPath.row-userTemplatesCount].rangeOfString(",") != nil {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let destinationVC = storyboard.instantiateViewControllerWithIdentifier("ModalViewControllerID") as! ModalViewController
            destinationVC.modalPresentationStyle = UIModalPresentationStyle.Custom
            transition = CustomTransition()
            transition.duration = 0.4
            destinationVC.transitioningDelegate = transition
            
            // send multiple forms list to modal controller
            var ownersModal = [FormTemplate]()
            var formsIdArray = split(multipleForms[indexPath.row-userTemplatesCount]) {$0 == ","}
            for ownerObj in ownersTemplates {
                for formId in formsIdArray {
                    if ownerObj.objectId == formId {
                        ownersModal.append(ownerObj)
                    }
                }
            }
            destinationVC.ownersList = ownersModal
            destinationVC.view.setHeight(CGFloat(ownersModal.count * 45 + 60))
            destinationVC.tableView.setHeight(CGFloat(ownersModal.count * 50 ))
            self.presentViewController(destinationVC, animated: true, completion: nil)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    /*
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
    // Get the new view controller using segue.destinationViewController.
    // Pass the selected object to the new view controller.
    }
    */
    
}
