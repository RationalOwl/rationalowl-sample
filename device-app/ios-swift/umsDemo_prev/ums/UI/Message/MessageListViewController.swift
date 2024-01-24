import Foundation
import RxCocoa
import RxSwift
import UIKit

import PaddingLabel

class MessageListViewController : UITableViewController
{
    private let messageCellId = "MessageCell"
    private let checkableMessageCellId = "CheckableMessageCell"
    
    @IBOutlet private var buttonSelectAll: UIBarButtonItem!
    @IBOutlet private var labelUnreadCount: UIBarButtonItem!
    @IBOutlet private var buttonMore: UIBarButtonItem!
    @IBOutlet private var buttonCancel: UIBarButtonItem!
    @IBOutlet private var buttonDelete: UIBarButtonItem!
    
    private let viewModel = MessageListViewModel()
    private var itemsSubscription: Disposable?
    private let disposeBag = DisposeBag()
    
    var message: Message?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        tableView.dataSource = nil
        tableView.delegate = nil
        
        if #available(iOS 14.0, *)
        {
            buttonMore.menu = UIMenu(title: "", children: [
                UIAction(title: "delete".localize()) { [weak self] _ in
                    self?.viewModel.viewType.accept(.delete)
                },
                UIAction(title: "mark_all_as_read".localize()) { _ in
                    MessagesRepository.shared.setAllAsRead()
                },
                UIAction(title: "user_info".localize()) { [weak self] _ in
                    self?.showUserInfo()
                }
            ])
        }
        else
        {
            buttonMore.target = self
            buttonMore.action = #selector(onButtonMoreClick(_:))
        }
        
        viewModel.viewType.subscribe(on: MainScheduler.instance).bind { [weak self] viewType in
            guard let self = self else { return }
            
            self.title = (viewType == .normal ? "messages" : "delete_messages").localize()
            
            if viewType == .normal
            {
                self.navigationItem.leftBarButtonItem = nil
                self.updateUnreadLabel()
            }
            else
            {
                self.navigationItem.leftBarButtonItem = self.buttonSelectAll
                self.navigationItem.rightBarButtonItems = [self.buttonCancel]
            }
            
            self.itemsSubscription?.dispose()
            
            let cellId: String
            let cellType: UITableViewCell.Type
            
            if viewType == .normal
            {
                cellId = self.messageCellId
                cellType = MessageTableViewCell.self
            }
            else
            {
                cellId = self.checkableMessageCellId
                cellType = CheckableMessageTableViewCell.self
            }
            
            self.itemsSubscription = self.viewModel.items.bind(to: self.tableView.rx.items(cellIdentifier: cellId, cellType: cellType)) { [weak self] (row: Int, element, cell) in
                guard let self = self else { return }
                
                let message = self.viewModel.items.value[row]
                self.buildCell(cell as! MessageTableViewCellProtocol, message)
            }
            self.itemsSubscription!.disposed(by: self.disposeBag)
            
            self.tableView.setEditing(viewType == .delete, animated: true)
            self.navigationController?.isToolbarHidden = viewType == .normal
        }.disposed(by: disposeBag)
        
        viewModel.items.bind { [weak self] items in
            guard let self = self else { return }
            let selectedItemIds: [String] = self.viewModel.selectedItemIds.value
            
            self.buttonSelectAll.isEnabled = !items.isEmpty
            self.buttonSelectAll.title = (items.isEmpty || selectedItemIds.count < items.count ? "select_all" : "deselect_all").localize()
            
            self.updateListBackground()
            self.updateUnreadLabel()
        }.disposed(by: disposeBag)
        
        viewModel.selectedItemIds.bind { [weak self] checkedItemIds in
            guard let self = self else { return }
            if self.viewModel.viewType.value != .delete { return }
            
            let items: [Message] = self.viewModel.items.value
            
            self.title = !checkedItemIds.isEmpty ? "delete_messages_selection_count".localize(checkedItemIds.count) : "delete_messages".localize()
            self.buttonSelectAll.title = (items.isEmpty || checkedItemIds.count < items.count ? "select_all" : "deselect_all").localize()
            self.buttonDelete.isEnabled = !checkedItemIds.isEmpty
        }.disposed(by: disposeBag)
        
        tableView.rx.modelSelected(Message.self).subscribe { [weak self] message in
            guard let self = self else { return }
            
            if (self.viewModel.viewType.value == .normal)
            {
                self.showMessageView(message)
            }
            else
            {
                self.viewModel.setSelectedState(message.id, selected: true)
            }
        }.disposed(by: disposeBag)
        
        _ = tableView.rx.modelDeselected(Message.self).subscribe { [weak self] message in
            guard let self = self else { return }
            if self.viewModel.viewType.value != .delete { return }
            
            self.viewModel.setSelectedState(message.id, selected: false)
        }.disposed(by: disposeBag)
    }
    
    override func viewWillAppear(_ animated: Bool)
    {
        navigationController?.isToolbarHidden = viewModel.viewType.value == .normal
    }
    
    private func updateListBackground()
    {
        if viewModel.items.value.isEmpty
        {
            let label = UILabel()
            label.text = "no_messages".localize()
            label.font = UIFont.systemFont(ofSize: 14)
            label.textColor = .gray
            label.textAlignment = .center
            
            tableView.backgroundView = label
            tableView.separatorStyle = .none
        }
        else
        {
            tableView.backgroundView = nil
            tableView.separatorStyle = .singleLine
        }
    }
    
    private func updateUnreadLabel()
    {
        let unreadCount: Int = viewModel.items.value.filter({ !$0.isRead }).count
        
        if unreadCount > 0
        {
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
            
            if textSize.width < textSize.height
            {
                labelUnreadCount.widthAnchor.constraint(equalToConstant: textSize.height + 8).isActive = true
            }
            
            let unreadStack = UIStackView(arrangedSubviews: [
                labelUnread,
                UIView.spacer(for: .horizontal),
                labelUnreadCount
            ])
            unreadStack.axis = .horizontal
            
            let unreadStackButton = UIBarButtonItem(customView: unreadStack)
            
            navigationItem.rightBarButtonItems = [buttonMore, unreadStackButton]
        }
        else
        {
            navigationItem.rightBarButtonItems = [buttonMore]
        }
    }
    
    private func buildCell(_ cell: MessageTableViewCellProtocol, _ message: Message)
    {
        if message.type == .emergency
        {
            cell.imageViewIcon.image = UIImage(named: "MessageOrangeImage")
            cell.labelTitle.text = "message_emergency_title".localize(message.title)
        }
        else
        {
            cell.imageViewIcon.image = UIImage(named: "MessageGreenImage")
            cell.labelTitle.text = "message_normal_title".localize(message.title)
        }
        
        cell.labelBody.text = message.body
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = (Calendar.current.isDateInToday(message.receivedAt) ? "message_today_datetime_format" : "message_past_datetime_format").localize()
        cell.labelReceivedTime.text = dateFormatter.string(from: message.receivedAt)
        
        cell.labelStatus.backgroundColor = UIColor(named: message.isRead ? "ReadMessageColor" : "UnreadMessageColor")
        cell.labelStatus.text = (message.isRead ? "message_read" : "message_unread").localize()
    }
    
    @objc private func onButtonMoreClick(_ sender: UIBarButtonItem)
    {
        let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        
        let deleteAction = UIAlertAction(title: "delete".localize(), style: .default) { [weak self] _ in
            self?.viewModel.viewType.accept(.delete)
        }
        deleteAction.isEnabled = !viewModel.items.value.isEmpty
        actionSheet.addAction(deleteAction)
        
        let markAllAsReadAction = UIAlertAction(title: "mark_all_as_read".localize(), style: .default) { _ in
            MessagesRepository.shared.setAllAsRead()
        }
        markAllAsReadAction.isEnabled = !viewModel.items.value.isEmpty
        actionSheet.addAction(markAllAsReadAction)
        
        let userInfoAction = UIAlertAction(title: "user_info".localize(), style: .default) { [weak self] _ in
            self?.showUserInfo()
        }
        actionSheet.addAction(userInfoAction)
        
        actionSheet.addAction(UIAlertAction(title: "cancel".localize(), style: .cancel))
        
        present(actionSheet, animated: true)
    }
    
    @IBAction func onButtonSelectAllClick(_ sender: Any)
    {
        if (viewModel.selectedItemIds.value.count < viewModel.items.value.count)
        {
            for row in 0 ..< tableView.numberOfRows(inSection: 0)
            {
                tableView.selectRow(at: IndexPath(row: row, section: 0), animated: true, scrollPosition: .none)
            }
            
            let messageIds: [String] = self.viewModel.items.value.map({ $0.id })
            self.viewModel.selectedItemIds.accept(messageIds)
        }
        else
        {
            tableView.selectRow(at: nil, animated: true, scrollPosition: .none)
            self.viewModel.selectedItemIds.accept([])
        }
    }
    
    @IBAction func onButtonCancelClick(_ sender: Any)
    {
        viewModel.viewType.accept(.normal)
    }
    
    @IBAction private func onButtonDeleteClick(_ sender: Any)
    {
        let checkedItemCount: Int = viewModel.selectedItemIds.value.count
        let alert = UIAlertController(title: nil, message: "delete_multiple_messages".localize(checkedItemCount), preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "delete".localize(), style: .destructive) { [weak self] _ in
            self?.viewModel.removeSelectedItems()
            self?.viewModel.viewType.accept(.normal)
        })
        alert.addAction(UIAlertAction(title: "cancel".localize(), style: .cancel))
        
        present(alert, animated: true)
    }
    
    private func showMessageView(_ message: Message)
    {
        let viewController = self.storyboard!.instantiateViewController(withIdentifier: "MessageViewVC") as! MessageViewController
        viewController.message = message
        
        MessageReadReceiptService.shared.markAsRead(message.id)
        
        self.navigationController!.pushViewController(viewController, animated: true)
    }
    
    private func showUserInfo()
    {
        let storyboard = UIStoryboard(name: "UserInfo", bundle: Bundle.main)
        let viewController = storyboard.instantiateViewController(withIdentifier: "UserInfoVC") as! UserInfoViewController
        viewController.user = UserRepository.shared.user
        
        self.navigationController!.pushViewController(viewController, animated: true)
    }
}
