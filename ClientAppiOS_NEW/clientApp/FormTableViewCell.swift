//
//  FormTableViewCell.swift
//  clientApp
//
//  Created by Edmond Pruteanu on 19/01/2016.
//  Copyright (c) 2016 Edmond Pruteanu. All rights reserved.
//

import UIKit

class FormTableViewCell: UITableViewCell {

    @IBOutlet weak var textField: UITextField!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
