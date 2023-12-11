import UIKit

extension String {
    func localize(_ arguments: CVarArg...) -> String
    {
        let localization: String = NSLocalizedString(self, comment: "")
        
        if arguments.isEmpty
        {
            return localization
        }
        
        return String(format: localization, arguments: arguments)
    }
    
    func calculateSize(_ font: UIFont) -> CGSize
    {
        let fontAttributes = [NSAttributedString.Key.font: font]
        return (self as NSString).size(withAttributes: fontAttributes)
    }
}
