//
//  TemplateTableViewCell.swift
//  clientApp
//
//  Created by Edmond Pruteanu on 03/10/2015.
//  Copyright (c) 2015 Edmond Pruteanu. All rights reserved.
//

import UIKit

class TemplateTableViewCell: PFTableViewCell {
    @IBOutlet weak var templateName:UITextView!
    var transition: CustomTransition!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)      
        
        // Configure the view for the selected state
    }
    
    func configure(#text: String?, placeholder: String) {
        templateName.text = text
        templateName.accessibilityValue = text
    }
    
}
