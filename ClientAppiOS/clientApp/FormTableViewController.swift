import UIKit

class FormTableViewController: PFQueryTableViewController, UITextFieldDelegate, TemplatesControllerDelegate {
    let cellIdentifier = "FormCell"
    var allCellsText = [String]()
    var connectDict = [Int:String]()
    var myProfileDict = [Int:String]()
    var formId = ""
    var myProfileId = "DoccgSzAtV"
    
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
        tableView.registerNib(UINib(nibName: "FormTableViewCell", bundle: nil), forCellReuseIdentifier: cellIdentifier)

        super.viewDidLoad()
        
        let button = UIButton()
        button.frame = CGRectMake(0, 0, 100, 40) as CGRect
        button.setTitle("Template", forState: UIControlState.Normal)
        button.addTarget(self, action: Selector("clickOnButton:"), forControlEvents: UIControlEvents.TouchUpInside)
        self.navigationItem.titleView = button
        self.navigationItem.hidesBackButton = true
        
        // Do any additional setup after loading the view.
    }
    
    func clickOnButton(button: UIButton) {
        var templatesTableVC:TemplatesTableViewController = TemplatesTableViewController(className: "Templates")
        self.navigationController?.pushViewController(templatesTableVC, animated: true)
    }
    
    override func queryForTable() -> PFQuery {
        let predicate = NSPredicate(format:"formId == '" + formId + "' OR formId == '" + myProfileId + "'")
        var query:PFQuery = PFQuery(className:self.parseClassName!, predicate: predicate)
        
        if (objects?.count == 0) {
            query.cachePolicy = PFCachePolicy.CacheThenNetwork
        }
        
        query.orderByAscending("position")
        
        return query
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath, object: PFObject?) -> PFTableViewCell? {
        
        var cell:FormTableViewCell? = tableView.dequeueReusableCellWithIdentifier(cellIdentifier) as? FormTableViewCell
        
        if cell == nil {
            cell = NSBundle.mainBundle().loadNibNamed("FormTableViewCell", owner: self, options: nil)[0] as? FormTableViewCell
        }
        if formId == "" {
            formId = myProfileId
        }
        if let pfObject = object {
            if formId == myProfileId {
                let fieldLabel = pfObject["label"] as? String
                let fieldValue = pfObject["value"] as? String
                cell?.textField?.text = fieldValue
                let fieldObj = FormField(label: fieldLabel!, value: fieldValue!)
                GlobalVariables.myProfile.append(fieldObj)
            }
            cell?.textField.delegate = self
            cell?.textField.becomeFirstResponder()
        }
        
        return cell
        
    }
    
    func textFieldDidEndEditing(textField: UITextField) {
        allCellsText.append(textField.text)
        println(allCellsText)
    }
    
    func templatesController(templatesVC: TemplatesTableViewController) {
        self.navigationController?.popViewControllerAnimated(true)
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
