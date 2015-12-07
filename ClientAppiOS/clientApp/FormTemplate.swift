import Foundation

class FormTemplate {
    var objectId: String
    var owner: String
    var type:String
    var name:String
    
    init(objectId: String, owner: String, type:String, name:String) {
        self.objectId = objectId
        self.owner = owner
        self.type = type
        self.name = name
    }
}
