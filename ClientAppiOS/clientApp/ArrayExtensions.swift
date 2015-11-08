import Foundation

extension Array {
    func ref (i:Int) -> T? {
        return 0 <= i && i < count ? self[i] : nil
    }
}