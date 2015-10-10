import UIKit

class TemplatesTableViewController: PFQueryTableViewController {
    let cellIdentifier:String = "TemplateCell"
    var owners = [Owner]()
    var indexZeroHeight = -1;
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
            var nameField:String? = pfObject["name"] as? String;
            if nameField != nil {
                cell?.templateName?.text = pfObject["name"] as? String
            } else {
                let ownerName:String? = pfObject["owner"] as? String
                let ownerId:String? = pfObject.objectId
                var exists:Bool = false
                for ownerObj in owners {
                    if ownerObj.owner == ownerName {
                        exists = true
                        break
                    }
                }
                if !exists {
                    cell?.templateName?.text = ownerName
                } else {
                    if !maxReached {
                        indexZeroHeight = indexPath.row
                    }
                }
                if ownerId != nil {
                    let ownerObj = Owner(objectId: ownerId!, owner: ownerName!)
                    owners.append(ownerObj)
                }
            }
        }
        
        return cell
        
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        var height:CGFloat = 50;
        if indexPath.row == indexZeroHeight {
            height = 0
        }
        return height;
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
