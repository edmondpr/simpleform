import Foundation
import UIKit

extension UIView {
    
    // MARK: - Frame
    
    /**
    Redefines the height of the view
    
    - parameter height: The new value for the view's height
    */
    func setHeight(height: CGFloat) {
        
        var frame: CGRect = self.frame
        frame.size.height = height
        
        self.frame = frame
    }
    
    /**
    Redefines the width of the view
    
    - parameter width: The new value for the view's width
    */
    func setWidth(width: CGFloat) {
        
        var frame: CGRect = self.frame
        frame.size.width = width
        
        self.frame = frame
    }
    
    /**
    Redefines X position of the view
    
    - parameter x: The new x-coordinate of the view's origin point
    */
    func setX(x: CGFloat) {
        
        var frame: CGRect = self.frame
        frame.origin.x = x
        
        self.frame = frame
    }
    
    /**
    Redefines Y position of the view
    
    - parameter y: The new y-coordinate of the view's origin point
    */
    func setY(y: CGFloat) {
        
        var frame: CGRect = self.frame
        frame.origin.y = y
        
        self.frame = frame
    }
}