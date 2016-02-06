//
//  FormTableViewCell.swift
//  clientApp
//
//  Created by Edmond Pruteanu on 13/09/2015.
//  Copyright (c) 2015 Edmond Pruteanu. All rights reserved.
//

import UIKit

class FormTableViewCell: PFTableViewCell {
    @IBOutlet weak var textField:UITextField!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func configure(#text: String?, placeholder: String) {
        textField.text = text
        textField.placeholder = placeholder
        
        textField.accessibilityValue = text
        textField.accessibilityLabel = placeholder
    }
    
}
