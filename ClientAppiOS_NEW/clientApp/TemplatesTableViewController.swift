import UIKit
import Parse

class TemplatesTableViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    var templates = [String]()
    var userTemplates = [FormTemplate]()
    var ownersTemplates = [MultipleFormTemplate]()
    var myProfileId = ""
    var tableView: UITableView!
    var selectedIndexPath: NSIndexPath?
    
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
        var query:PFQuery = PFQuery(className:"OwnersTemplates")
        query.findObjectsInBackgroundWithBlock {
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
        let ownerTemplateDB = FormTemplate(objectId: ownerId!, owner: ownerName!, type: ownerType!, name:"")
        
        var exists = false
        for ownerTemplate in ownersTemplates {
            if ownerTemplate.firstTemplate.owner == ownerName {
                exists = true
                ownerTemplate.otherTemplates.append(ownerTemplateDB)
                break
            }
        }
        
        if !exists {
            let multipleFormTemplate = MultipleFormTemplate(firstTemplate: ownerTemplateDB, otherTemplates: [])
            ownersTemplates.append(multipleFormTemplate)
        }
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return ownersTemplates.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("TemplateTableViewCell", forIndexPath: indexPath) as! TemplateTableViewCell
        cell.textLabel?.text = ownersTemplates[indexPath.row].firstTemplate.owner
        return cell
    }
    
}