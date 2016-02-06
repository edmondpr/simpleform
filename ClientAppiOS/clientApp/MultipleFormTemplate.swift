import Foundation

class MultipleFormTemplate {
    var firstTemplate: FormTemplate
    var otherTemplates: [FormTemplate]
    
    init(firstTemplate: FormTemplate, otherTemplates: [FormTemplate]) {
        self.firstTemplate = firstTemplate
        self.otherTemplates = otherTemplates
    }
}
