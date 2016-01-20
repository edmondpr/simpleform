import UIKit
import Parse

class FormViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    var formFields = [FormField]()
    var myProfileId = "DoccgSzAtV"
    
    var tableView: UITableView!
    var selectedIndexPath: NSIndexPath?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView = UITableView(frame: view.frame)
        tableView.dataSource = self
        tableView.delegate = self
        let cellNib = UINib(nibName: "FormTableViewCell", bundle: NSBundle.mainBundle())
        tableView.registerNib(cellNib, forCellReuseIdentifier: "FormTableViewCell")
        view.addSubview(tableView)
        
        getFormFields()
    }
    
    func getFormFields() {
        let predicate = NSPredicate(format:"formId == '" + myProfileId + "'")
        var query:PFQuery = PFQuery(className:"ClientsFields", predicate: predicate)
        query.findObjectsInBackgroundWithBlock {
            (objects: [AnyObject]?, error: NSError?) -> Void in
            
            if error == nil {
                // Do something with the found objects
                if let objects = objects as? [PFObject] {
                    for pfObject in objects {
                        let fieldLabel = pfObject["label"] as? String
                        let fieldValue = pfObject["value"] as? String
                        let fieldObj = FormField(label: fieldLabel!, value: fieldValue!)
                        self.formFields.append(fieldObj)
                    }
                    self.tableView.reloadData()
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