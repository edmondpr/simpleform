import UIKit
import Parse

class FormViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    var names = [String]()
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
                // The find succeeded.
                println("Successfully retrieved \(objects!.count) templates.")
                // Do something with the found objects
                if let objects = objects as? [PFObject] {
                    for object in objects {
                        self.names.append(object["value"] as! String)
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
        return names.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("FormTableViewCell", forIndexPath: indexPath) as! FormTableViewCell
        cell.textField?.text = names[indexPath.row]
        return cell
    }
    
}