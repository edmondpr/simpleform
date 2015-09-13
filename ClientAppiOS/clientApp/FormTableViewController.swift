import UIKit

class FormTableViewController: PFQueryTableViewController, UITextFieldDelegate {
    let cellIdentifier:String = "FormCell"
    var allCellsText = [String]()
    
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
        
        // Do any additional setup after loading the view.
    }
    
    override func queryForTable() -> PFQuery {
        var query:PFQuery = PFQuery(className:self.parseClassName!)
        query.whereKey("formId", equalTo: "tPm8gXTUv1")
        
        if (objects?.count == 0) {
            query.cachePolicy = PFCachePolicy.CacheThenNetwork
        }
        
        query.orderByAscending("position")
        
        return query
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath, object: PFObject?) -> PFTableViewCell? {
        
        var cell:FormTableViewCell? = tableView.dequeueReusableCellWithIdentifier(cellIdentifier) as? FormTableViewCell
        
        if(cell == nil) {
            cell = NSBundle.mainBundle().loadNibNamed("FormTableViewCell", owner: self, options: nil)[0] as? FormTableViewCell
        }
        
        if let pfObject = object {
            cell?.textField?.text = pfObject["value"] as? String
            cell?.textField.delegate = self
            cell?.textField.becomeFirstResponder()
        }
        
        return cell
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func textFieldDidEndEditing(textField: UITextField) {
        allCellsText.append(textField.text)
        println(allCellsText)
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
