import UIKit
import ParseUI

public class TextInputTableViewCell: PFTableViewCell {
	@IBOutlet weak var textField: UITextField!
	
	public func configure(#text: String?, placeholder: String) {
		textField.text = text
		textField.placeholder = placeholder
		
		textField.accessibilityValue = text
		textField.accessibilityLabel = placeholder
	}
}