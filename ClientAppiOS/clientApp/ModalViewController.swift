//
//  SecondViewController.swift
//  modalTransitions
//
//  Created by Daniel Eden on 8/25/14.
//  Copyright (c) 2014 Daniel Eden. All rights reserved.
//

import UIKit

class ModalViewController: ViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        view.layer.cornerRadius = 6
        view.layer.shadowColor = UIColor.blackColor().CGColor
        view.layer.shadowOpacity = 0.5
        view.layer.shadowOffset = CGSizeMake(0, 10)
        view.layer.shadowRadius = 10
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onDismissTouch(sender: AnyObject) {
        dismissViewControllerAnimated(true, completion: nil)
    }
    
}
