import UIKit
import Parse

class FormTableViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    var formFields = [FormField]()
    var isOwner = false
    var formId = ""
    
    var tableView: UITableView!
    var selectedIndexPath: NSIndexPath?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setHeader()
        loadTableView()
        getFormFields()
    }
    
    func setHeader() {
        let button = UIButton()
        button.frame = CGRectMake(0, 0, 100, 40) as CGRect
        button.setTitle("Form", forState: UIControlState.Normal)
        button.addTarget(self, action: Selector("clickOnButton:"), forControlEvents: UIControlEvents.TouchUpInside)
        self.navigationItem.titleView = button
        self.navigationItem.hidesBackButton = true
    }
    
    func clickOnButton(button: UIButton) {
        var templatesTableVC:TemplatesTableViewController = TemplatesTableViewController()
        self.navigationController?.pushViewController(templatesTableVC, animated: true)
    }
    
    func loadTableView() {
        tableView = UITableView(frame: view.frame)
        tableView.dataSource = self
        tableView.delegate = self
        let cellNib = UINib(nibName: "FormTableViewCell", bundle: NSBundle.mainBundle())
        tableView.registerNib(cellNib, forCellReuseIdentifier: "FormTableViewCell")
        view.addSubview(tableView)
    }
    
    func getFormFields() {
        var className = "ClientsFields"
        if isOwner {
            className = "OwnersFields"
        }
        if formId == "" {
            formId = GlobalVariables.myProfileId
        }
        let predicate = NSPredicate(format: "formId == '" + formId + "'")
        var query:PFQuery = PFQuery(className: className, predicate: predicate)
        query.findObjectsInBackgroundWithBlock {
            (objects: [AnyObject]?, error: NSError?) -> Void in
            
            if error == nil {
                if let objects = objects as? [PFObject] {
                    for pfObject in objects {
                        let fieldLabel = pfObject["label"] as? String
                        var fieldValue = pfObject["value"] as? String
                        // Connect fields from my profile
                        if self.formId != GlobalVariables.myProfileId {
                            let fieldConnect = pfObject["connect"] as? String
                            if fieldConnect != nil && fieldConnect != "" {
                                let startIndex = advance(fieldConnect!.startIndex, 2)
                                let endIndex = advance(fieldConnect!.endIndex, -2)
                                let range = startIndex..<endIndex
                                var fieldConnectStripped = fieldConnect!.substringWithRange(range)
                                for myProfileField in GlobalVariables.myProfile {
                                    if myProfileField.label == fieldConnectStripped {
                                        fieldValue = myProfileField.value
                                        break
                                    }
                                }
                            }
                        }
                        if fieldValue == nil {
                            fieldValue = ""
                        }
                        let fieldObj = FormField(label: fieldLabel!, value: fieldValue!)
                        self.formFields.append(fieldObj)
                    }
                    self.tableView.reloadData()
                    if self.formId == GlobalVariables.myProfileId {
                        GlobalVariables.myProfile = self.formFields
                    }
                }
            } else {
                // Log details of the failure
                println("Error: \(error!) \(error!.userInfo!)")
            }
        }
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return formFields.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("FormTableViewCell", forIndexPath: indexPath) as! FormTableViewCell
        cell.textField?.placeholder = formFields[indexPath.row].label
        cell.textField?.text = formFields[indexPath.row].value
        return cell
    }
    
}