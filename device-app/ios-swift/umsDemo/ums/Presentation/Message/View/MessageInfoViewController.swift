import Foundation
import UIKit

class MessageInfoViewController: UIViewController {
    var message: DataDef.Message!

    override func viewDidLoad() {
        super.viewDidLoad()

        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "message_long_datetime_format".localize()

        let pushSentHeader = buildLabel("push_sent".localize(), isCaption: true)
        let pushSent = buildLabel(dateFormatter.string(from: message.sentAt))

        let pushReceivedHeader = buildLabel("push_received".localize(), isCaption: true)
        let pushReceived = buildLabel(dateFormatter.string(from: message.receivedAt))

        let talkSentHeader = buildLabel("talk_sent".localize(), isCaption: true)
        let talkSent = buildLabel(message.alimTalkSentAt != nil ? dateFormatter.string(from: message.alimTalkSentAt!) : "not_sent".localize())

        let textSentHeader = buildLabel("text_sent".localize(), isCaption: true)
        let textSent = buildLabel(message.munjaSentAt != nil ? dateFormatter.string(from: message.munjaSentAt!) + (message.munjaType != nil ? " (\(message.munjaType!))" : "") : "not_sent".localize())

        let stackView = UIStackView(arrangedSubviews: [
            pushSentHeader, pushSent,
            UIView.spacer(),
            pushReceivedHeader, pushReceived,
            UIView.spacer(),
            talkSentHeader, talkSent,
            UIView.spacer(),
            textSentHeader, textSent,
        ])

        stackView.alignment = .leading
        stackView.axis = .vertical
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = UIEdgeInsets(top: 0, left: 16, bottom: 16, right: 16)

        view = stackView
    }

    private func buildLabel(_ text: String, isCaption: Bool = false) -> UILabel {
        let label = UILabel()

        if isCaption {
            label.textColor = UIColor.secondaryLabel
            label.font = label.font.withSize(12)
        }

        label.text = text

        return label
    }
}
