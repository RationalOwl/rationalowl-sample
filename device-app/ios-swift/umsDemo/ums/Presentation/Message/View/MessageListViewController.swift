import Foundation
import UIKit

class MessageListViewController: UITableViewController {
    private static let messageCellId = "MessageCell"
    private static let checkableMessageCellId = "CheckableMessageCell"

    @IBOutlet private var buttonSelectAll: UIBarButtonItem!
    @IBOutlet private var labelUnreadCount: UIBarButtonItem!
    @IBOutlet private var buttonMore: UIBarButtonItem!
    @IBOutlet private var buttonCancel: UIBarButtonItem!
    @IBOutlet private var buttonDelete: UIBarButtonItem!

    private let viewModel = MessageListViewModel()

    var message: DataDef.Message?

    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.dataSource = self
        tableView.delegate = self

        if #available(iOS 14.0, *) {
            buttonMore.menu = UIMenu(title: "", children: [
                UIAction(title: "delete".localize()) { [weak self] _ in
                    self?.viewModel.viewType.value = .delete
                },
                UIAction(title: "mark_all_as_read".localize()) { _ in
                    MessageRepository.shared.markAllAsRead()
                },
                UIAction(title: "user_info".localize()) { [weak self] _ in
                    self?.showUserInfo()
                },
            ])
        } else {
            buttonMore.target = self
            buttonMore.action = #selector(onButtonMoreClick(_:))
        }

        viewModel.viewType.observe(on: self) { [weak self] viewType in
            self?.onViewTypeChange(viewType)
        }

        viewModel.items.observe(on: self) { [weak self] items in
            self?.onItemsChange(items)
        }

        viewModel.selectedItemIds.observe(on: self) { [weak self] selectedItemIds in
            self?.onSelectedItemsChange(selectedItemIds)
        }
    }

    override func viewWillAppear(_: Bool) {
        navigationController?.isToolbarHidden = viewModel.viewType.value == .normal
    }

    private func showMessageView(_ message: DataDef.Message) {
        let viewController = storyboard!.instantiateViewController(withIdentifier: "MessageReadVC") as! MessageReadViewController
        viewController.viewModel = MessageReadViewModel(message)

        navigationController!.pushViewController(viewController, animated: true)

        MessageRepository.shared.markAsRead(message)
    }

    private func showUserInfo() {
        let storyboard = UIStoryboard(name: "UserInfo", bundle: Bundle.main)
        let viewController = storyboard.instantiateViewController(withIdentifier: "UserInfoVC") as! UserInfoViewController
        viewController.user = UserLocalDataSource.shared.user

        navigationController!.pushViewController(viewController, animated: true)
    }

    // MARK: ViewModel observers

    private func onViewTypeChange(_ viewType: MessageListViewModel.ViewType) {
        title = (viewType == .normal ? "messages" : "delete_messages").localize()

        if viewType == .normal {
            navigationItem.leftBarButtonItem = nil
            updateUnreadLabel()
        } else {
            navigationItem.leftBarButtonItem = buttonSelectAll
            navigationItem.rightBarButtonItems = [buttonCancel]
        }

        tableView.setEditing(viewType == .delete, animated: true)
        navigationController?.isToolbarHidden = viewType == .normal
    }

    private func onItemsChange(_ items: [DataDef.Message]) {
        buttonSelectAll.isEnabled = !items.isEmpty
        buttonSelectAll.title = (items.isEmpty || viewModel.selectedItemIds.value.count < items.count ? "select_all" : "deselect_all").localize()

        updateListBackground()
        updateUnreadLabel()

        tableView.reloadData()
    }

    private func onSelectedItemsChange(_ selectedItemIds: Set<String>) {
        if viewModel.viewType.value == .delete {
            title = !selectedItemIds.isEmpty ? "delete_messages_selection_count".localize(selectedItemIds.count) : "delete_messages".localize()
            buttonSelectAll.title = (viewModel.items.value.isEmpty || selectedItemIds.count < viewModel.items.value.count ? "select_all" : "deselect_all").localize()
            buttonDelete.isEnabled = !selectedItemIds.isEmpty
        }
    }

    // MARK: UI
    private func updateListBackground() {
        if viewModel.items.value.isEmpty {
            let label = UILabel()
            label.text = "no_messages".localize()
            label.font = UIFont.systemFont(ofSize: 14)
            label.textColor = .gray
            label.textAlignment = .center

            tableView.backgroundView = label
            tableView.separatorStyle = .none
        } else {
            tableView.backgroundView = nil
            tableView.separatorStyle = .singleLine
        }
    }

    private func updateUnreadLabel() {
        let unreadCount: Int = viewModel.items.value.filter { !$0.isRead }.count

        if unreadCount > 0 {
            let labelUnread = UILabel()
            labelUnread.font = UIFont.systemFont(ofSize: 14, weight: .semibold)
            labelUnread.text = "message_unread".localize()
            labelUnread.textColor = UIColor(named: "UnreadMessageColor")

            let numberFont = UIFont.systemFont(ofSize: 12, weight: .semibold)
            let textSize = String(unreadCount).calculateSize(numberFont)
            let diameter: CGFloat = textSize.height + 4

            let labelUnreadCount = PaddingLabel()

            labelUnreadCount.topInset = 4
            labelUnreadCount.leftInset = 4
            labelUnreadCount.rightInset = 4
            labelUnreadCount.bottomInset = 4

            labelUnreadCount.bounds = CGRectMake(0, 0, diameter, diameter)
            labelUnreadCount.font = numberFont
            labelUnreadCount.layer.backgroundColor = UIColor(named: "UnreadMessageColor")?.cgColor
            labelUnreadCount.layer.cornerRadius = diameter / 2
            labelUnreadCount.text = unreadCount < 100 ? String(unreadCount) : "99+"
            labelUnreadCount.textColor = UIColor.white
            labelUnreadCount.textAlignment = .center

            if textSize.width < textSize.height {
                labelUnreadCount.widthAnchor.constraint(equalToConstant: textSize.height + 8).isActive = true
            }

            let unreadStack = UIStackView(arrangedSubviews: [
                labelUnread,
                UIView.spacer(for: .horizontal),
                labelUnreadCount,
            ])
            unreadStack.axis = .horizontal

            let unreadStackButton = UIBarButtonItem(customView: unreadStack)

            navigationItem.rightBarButtonItems = [buttonMore, unreadStackButton]
        } else {
            navigationItem.rightBarButtonItems = [buttonMore]
        }
    }

    // MARK: Event handlers
    @objc private func onButtonMoreClick(_: UIBarButtonItem) {
        let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)

        // Delete
        let deleteAction = UIAlertAction(title: "delete".localize(), style: .default) { [weak self] _ in
            self?.viewModel.viewType.value = .delete
        }
        deleteAction.isEnabled = !viewModel.items.value.isEmpty
        actionSheet.addAction(deleteAction)

        // Mark as read
        let markAllAsReadAction = UIAlertAction(title: "mark_all_as_read".localize(), style: .default) { _ in
            MessageRepository.shared.markAllAsRead()
        }
        markAllAsReadAction.isEnabled = !viewModel.items.value.isEmpty
        actionSheet.addAction(markAllAsReadAction)

        // User info
        let userInfoAction = UIAlertAction(title: "user_info".localize(), style: .default) { [weak self] _ in
            self?.showUserInfo()
        }
        actionSheet.addAction(userInfoAction)

        // Cancel
        actionSheet.addAction(UIAlertAction(title: "cancel".localize(), style: .cancel))

        present(actionSheet, animated: true)
    }

    @IBAction private func onButtonSelectAllClick(_: Any) {
        if viewModel.selectedItemIds.value.count < viewModel.items.value.count {
            for row in 0 ..< tableView.numberOfRows(inSection: 0) {
                tableView.selectRow(at: IndexPath(row: row, section: 0), animated: true, scrollPosition: .none)
            }

            let messageIds = Set<String>(viewModel.items.value.map { $0.id })
            viewModel.selectedItemIds.value = messageIds
        } else {
            tableView.selectRow(at: nil, animated: true, scrollPosition: .none)
            viewModel.selectedItemIds.value = []
        }
    }

    @IBAction private func onButtonCancelClick(_: Any) {
        viewModel.viewType.value = .normal
    }

    @IBAction private func onButtonDeleteClick(_: Any) {
        let checkedItemCount: Int = viewModel.selectedItemIds.value.count
        let alert = UIAlertController(title: nil, message: "delete_multiple_messages".localize(checkedItemCount), preferredStyle: .alert)

        alert.addAction(UIAlertAction(title: "delete".localize(), style: .destructive) { [weak self] _ in
            self?.viewModel.removeSelectedItems()
            self?.viewModel.viewType.value = .normal
        })
        alert.addAction(UIAlertAction(title: "cancel".localize(), style: .cancel))

        present(alert, animated: true)
    }
}

extension MessageListViewController {
    override func tableView(_: UITableView, numberOfRowsInSection _: Int) -> Int {
        return viewModel.items.value.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let message = viewModel.items.value[indexPath.row]
        let cellId = viewModel.viewType.value == .normal ? Self.messageCellId : Self.checkableMessageCellId
        let cell = tableView.dequeueReusableCell(withIdentifier: cellId, for: indexPath) as! MessageTableViewCellProtocol

        if message.type == .emergency {
            cell.imageViewIcon.image = UIImage(named: "MessageOrangeImage")
            cell.labelTitle.text = "message_emergency_title".localize(message.title)
        } else {
            cell.imageViewIcon.image = UIImage(named: "MessageGreenImage")
            cell.labelTitle.text = "message_normal_title".localize(message.title)
        }

        cell.labelBody.text = message.body

        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = (Calendar.current.isDateInToday(message.receivedAt) ? "message_today_datetime_format" : "message_past_datetime_format").localize()
        cell.labelReceivedTime.text = dateFormatter.string(from: message.receivedAt)

        cell.labelStatus.backgroundColor = UIColor(named: message.isRead ? "ReadMessageColor" : "UnreadMessageColor")
        cell.labelStatus.text = (message.isRead ? "message_read" : "message_unread").localize()

        return cell as! UITableViewCell
    }

    override func tableView(_: UITableView, didSelectRowAt indexPath: IndexPath) {
        let message = viewModel.items.value[indexPath.row]

        if viewModel.viewType.value == .normal {
            showMessageView(message)
        } else {
            viewModel.setSelectedState(message.id, selected: true)
        }
    }

    override func tableView(_: UITableView, didDeselectRowAt indexPath: IndexPath) {
        if viewModel.viewType.value == .delete {
            let message = viewModel.items.value[indexPath.row]
            viewModel.setSelectedState(message.id, selected: false)
        }
    }
}
