import UIKit

// https://stackoverflow.com/questions/66391184/how-to-create-the-equivalent-of-a-swiftui-stack-view-spacer-programmatically
extension UIView {
    static func spacer(size: CGFloat = 8, for layout: NSLayoutConstraint.Axis = .vertical) -> UIView
    {
        let spacer = UIView()
        
        if layout == .horizontal
        {
            spacer.widthAnchor.constraint(equalToConstant: size).isActive = true
        }
        else
        {
            spacer.heightAnchor.constraint(equalToConstant: size).isActive = true
        }
        
        return spacer
    }
}
