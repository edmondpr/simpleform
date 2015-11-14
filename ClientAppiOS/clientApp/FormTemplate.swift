import Foundation

class FormTemplate {
    var objectId: String
    var owner: String
    var type:String
    
    init(objectId: String, owner: String, type:String) {
        self.objectId = objectId
        self.owner = owner
        self.type = type
    }
}
