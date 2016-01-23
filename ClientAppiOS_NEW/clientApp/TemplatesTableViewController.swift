import UIKit
import Parse

class TemplatesTableViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    var templates = [MultipleFormTemplate]()
    var tableView: UITableView!
    var selectedIndexPath: NSIndexPath?
    var transition: CustomTransition!
    
    let CellIdentifier = "TemplateTableViewCell"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setHeader()
        loadTableView()
        getTemplates()
    }
    
    func setHeader() {
        self.navigationItem.title = "All Forms"
    }
    
    func loadTableView() {
        tableView = UITableView(frame: view.frame)
        tableView.dataSource = self
        tableView.delegate = self
        let cellNib = UINib(nibName: "TemplateTableViewCell", bundle: NSBundle.mainBundle())
        tableView.registerNib(cellNib, forCellReuseIdentifier: "TemplateTableViewCell")
        view.addSubview(tableView)
    }
    
    func getTemplates() {
        let predicate = NSPredicate(format: "user == '" + GlobalVariables.user + "'")
        var clientQuery:PFQuery = PFQuery(className: "ClientsTemplates", predicate: predicate)
        clientQuery.addAscendingOrder("name")
        clientQuery.findObjectsInBackgroundWithBlock {
            (objects: [AnyObject]?, error: NSError?) -> Void in
            
            if error == nil {
                // Do something with the found objects
                if let objects = objects as? [PFObject] {
                    for pfObject in objects {
                        self.addClientTemplate(pfObject)
                    }
                    self.addOwnersHeader()
                    self.getOwnersTemplates()
                }
            } else {
                // Log details of the failure
                println("Error: \(error!) \(error!.userInfo!)")
            }
        }
    }
    
    func addClientTemplate(pfObject:PFObject) {
        let clientName:String? = pfObject["name"] as? String
        let clientId:String? = pfObject.objectId
        let clientTemplateDB = FormTemplate(objectId: clientId!, owner: "", type: "", name: clientName!)
        let multipleFormTemplate = MultipleFormTemplate(firstTemplate: clientTemplateDB, otherTemplates: [])
        templates.append(multipleFormTemplate)
    }
    
    func addOwnersHeader() {
        let header = FormTemplate(objectId: "", owner: "Available Forms", type: "", name: "")
        let headerTemplate = MultipleFormTemplate(firstTemplate: header, otherTemplates: [])
        templates.append(headerTemplate)
    }
    
    func getOwnersTemplates() {
        var ownersQuery:PFQuery = PFQuery(className:"OwnersTemplates")
        ownersQuery.addAscendingOrder("owner")
        ownersQuery.findObjectsInBackgroundWithBlock {
            (objects: [AnyObject]?, error: NSError?) -> Void in
            
            if error == nil {
                // Do something with the found objects
                if let objects = objects as? [PFObject] {
                    for pfObject in objects {
                        self.addOwnerTemplate(pfObject)
                    }
                    self.tableView.reloadData()
                }
            } else {
                // Log details of the failure
                println("Error: \(error!) \(error!.userInfo!)")
            }
        }
    }
    
    func addOwnerTemplate(pfObject:PFObject) {
        let ownerName:String? = pfObject["owner"] as? String
        let ownerType:String? = pfObject["type"] as? String
        let ownerId:String? = pfObject.objectId
        let ownerTemplateDB = FormTemplate(objectId: ownerId!, owner: ownerName!, type: ownerType!, name: "")
        
        var exists = false
        for ownerTemplate in templates {
            if ownerTemplate.firstTemplate.owner == ownerName {
                exists = true
                ownerTemplate.otherTemplates.append(ownerTemplateDB)
                break
            }
        }
        if !exists {
            let multipleFormTemplate = MultipleFormTemplate(firstTemplate: ownerTemplateDB, otherTemplates: [])
            templates.append(multipleFormTemplate)
        }
    }
    
    func goToForm(formId: String, isOwner: Bool){
        var formTableVC:FormTableViewController = FormTableViewController()
        formTableVC.formId = formId
        formTableVC.isOwner = isOwner
        self.navigationController?.pushViewController(formTableVC, animated: true)
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return templates.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("TemplateTableViewCell", forIndexPath: indexPath) as! TemplateTableViewCell
        if templates[indexPath.row].firstTemplate.name != "" {
            cell.textLabel?.text = templates[indexPath.row].firstTemplate.name
        } else {
            cell.textLabel?.text = templates[indexPath.row].firstTemplate.owner
            // Set owners header style
            if templates[indexPath.row].firstTemplate.objectId == "" {
                cell.textLabel?.font = cell.textLabel?.font.fontWithSize(12)
                cell.textLabel?.textColor = UIColor.redColor()
            }
        }
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if templates[indexPath.row].firstTemplate.name != "" {
            loadClientTemplate(indexPath)
        } else {
            if templates[indexPath.row].firstTemplate.objectId != "" {
                openOwnerModal(indexPath)
            }
        }
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        var height:CGFloat = 40
        if templates[indexPath.row].firstTemplate.objectId == "" {
            height = 30
        }
        return height
    }
    
    func loadClientTemplate(indexPath: NSIndexPath) {
        var formTableVC:FormTableViewController = FormTableViewController()
        formTableVC.isOwner = false
        formTableVC.formId = templates[indexPath.row].firstTemplate.objectId
        self.navigationController?.pushViewController(formTableVC, animated: true)
    }
    
    func openOwnerModal(indexPath: NSIndexPath) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let destinationVC = storyboard.instantiateViewControllerWithIdentifier("ModalViewControllerID") as! ModalViewController
        destinationVC.modalPresentationStyle = UIModalPresentationStyle.Custom
        transition = CustomTransition()
        transition.duration = 0.4
        destinationVC.transitioningDelegate = transition
        
        // send multiple forms list to modal controller
        var ownersModal = [FormTemplate]()
        ownersModal.append(templates[indexPath.row].firstTemplate)
        ownersModal.extend(templates[indexPath.row].otherTemplates)
        destinationVC.ownersList = ownersModal
        destinationVC.view.setHeight(CGFloat(ownersModal.count * 45 + 60))
        destinationVC.tableView.setHeight(CGFloat(ownersModal.count * 50 ))
        destinationVC.pViewController = self
        self.presentViewController(destinationVC, animated: true, completion: nil)
    }
}