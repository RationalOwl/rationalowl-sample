import UIKit

protocol MessageTableViewCellProtocol {
    var imageViewIcon: UIImageView { get }
    var labelTitle: UILabel { get }
    var labelBody: UILabel { get }
    var labelReceivedTime: UILabel { get }
    var labelStatus: UILabel { get }
}

class MessageTableViewCell: UITableViewCell, MessageTableViewCellProtocol {
    @IBOutlet private var _imageViewIcon: UIImageView!
    @IBOutlet private var _labelTitle: UILabel!
    @IBOutlet private var _labelBody: UILabel!
    @IBOutlet private var _labelReceivedTime: UILabel!
    @IBOutlet private var _labelStatus: UILabel!

    var imageViewIcon: UIImageView { return _imageViewIcon }

    var labelTitle: UILabel { return _labelTitle }

    var labelBody: UILabel { return _labelBody }

    var labelReceivedTime: UILabel { return _labelReceivedTime }

    var labelStatus: UILabel { return _labelStatus }
}

class CheckableMessageTableViewCell: UITableViewCell, MessageTableViewCellProtocol {
    @IBOutlet private var _imageViewIcon: UIImageView!
    @IBOutlet private var _labelTitle: UILabel!
    @IBOutlet private var _labelBody: UILabel!
    @IBOutlet private var _labelReceivedTime: UILabel!
    @IBOutlet private var _labelStatus: UILabel!

    var imageViewIcon: UIImageView { return _imageViewIcon }

    var labelTitle: UILabel { return _labelTitle }

    var labelBody: UILabel { return _labelBody }

    var labelReceivedTime: UILabel { return _labelReceivedTime }

    var labelStatus: UILabel { return _labelStatus }
}
