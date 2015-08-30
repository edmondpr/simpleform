import UIKit

public class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate {
    @IBOutlet weak var tableView: UITableView!
    var allCellsText = [String]()
    
	public func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return 3
	}
 
	public func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        var elem = "TextInputCell" + String(indexPath.row + 1)
        let cell = tableView.dequeueReusableCellWithIdentifier(elem) as! TextInputTableViewCell
        
        if indexPath.row > 1 {
            cell.hidden = true
        }
        
        cell.textField.delegate = self
        cell.textField.text = elem
        cell.textField.becomeFirstResponder()
        
		cell.configure(text: "\(indexPath.row + 1)", placeholder: "Enter some text!")
		return cell
	}
    
    @IBAction func getValue(sender : AnyObject) {
        tableView.reloadData()
    }
    
    public func textFieldDidEndEditing(textField: UITextField) {
        allCellsText.append(textField.text)
        println(allCellsText)
    }
    
}