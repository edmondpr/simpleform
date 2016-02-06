import UIKit

class ModalViewController: ViewController, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate {
    var ownersList = [FormTemplate]()
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var tableHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var dismiss: UIButton!
    weak var pViewController: TemplatesTableViewController?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.layer.cornerRadius = 6
        view.layer.shadowColor = UIColor.blackColor().CGColor
        view.layer.shadowOpacity = 0.5
        view.layer.shadowOffset = CGSizeMake(0, 10)
        view.layer.shadowRadius = 10
        
        self.tableView.registerClass(UITableViewCell.self, forCellReuseIdentifier: "ownercell")
        
        tableView.delegate = self
        tableView.dataSource = self
        tableHeightConstraint.constant = CGFloat(self.ownersList.count * 45);
        
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return ownersList.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell{
        let cell : UITableViewCell = tableView.dequeueReusableCellWithIdentifier("ownercell", forIndexPath: indexPath) as! UITableViewCell
        
        cell.textLabel!.text = self.ownersList[indexPath.row].type
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        dismissViewControllerAnimated(true, completion: nil)
        pViewController!.goToForm(self.ownersList[indexPath.row].objectId)
    }
    
    @IBAction func onDismissTouch(sender: AnyObject) {
        dismissViewControllerAnimated(true, completion: nil)      
    }
    
}
