//
//  FormTableViewCell.swift
//  clientApp
//
//  Created by Edmond Pruteanu on 19/01/2016.
//  Copyright (c) 2016 Edmond Pruteanu. All rights reserved.
//

import UIKit
import MaterialKit

class FormTableViewCell: UITableViewCell {

    @IBOutlet var textField: MKTextField!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        textField.rippleLocation = .Left
        textField.floatingPlaceholderEnabled = true
        textField.layer.borderColor = UIColor.clearColor().CGColor
        textField.rippleLayerColor = UIColor.MKColor.LightGreen
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
}
