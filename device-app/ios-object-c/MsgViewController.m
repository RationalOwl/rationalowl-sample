//
//  MsgViewController.m
//  sample
//
//  Created by 김정도 on 2018. 1. 22..
//  Copyright © 2018년 Rationalowl. All rights reserved.
//

#import "MsgViewController.h"

@interface MsgViewController ()

@end

@implementation MsgViewController

@synthesize msgListView;
@synthesize inputMessageField;
@synthesize tView, messages;

#pragma mark -
#pragma mark IBActions
// view controller life cycle

- (void)viewDidLoad {
    [super viewDidLoad];
    inputMessageField.text = @"input data";
    messages = [[NSMutableArray alloc] init];
    
    self.tView.delegate = self;
    self.tView.dataSource = self;
    
    //set delegate
    MinervaManager* minMgr = [MinervaManager getInstance];
    [minMgr setMessageDelegate:self];
    
}

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}




#pragma mark -
#pragma mark IBActions
//send message

- (IBAction)sendUpstreamMsg:(id)sender {
    //NSString* svcId = @"d0a83353281e4b678774a0efa44fdd82";
    NSString* serverId = @"1fe45769e24348bfa501c32032958483";
    NSString* msg = inputMessageField.text;
    MinervaManager* mgr = [MinervaManager getInstance];
    [mgr sendUpstreamMsg:msg serverRegId:serverId];
    
    NSString* displayStr = [NSString stringWithFormat:@"=>%@  ", msg];
    //[self.messages insertObject:displayStr atIndex:0];
    [self.messages addObject:displayStr];
    [self.tView reloadData];
    NSIndexPath *topIndexPath = [NSIndexPath indexPathForRow:messages.count-1 inSection:0];
    [self.tView scrollToRowAtIndexPath:topIndexPath
                      atScrollPosition:UITableViewScrollPositionMiddle
                              animated:YES];
}



- (IBAction)sendP2PMsg:(id)sender {
    NSString* msg = inputMessageField.text;
    NSMutableArray* devices = [[NSMutableArray alloc] init];
    [devices addObject:@"cf12c6b3c46e4e318b6e3c77b0590b9d"];
    MinervaManager* mgr = [MinervaManager getInstance];
    [mgr sendP2PMsg:msg devices:devices];
    
    NSString* displayStr = [NSString stringWithFormat:@"=>(p2p)%@  ", msg];
    //[self.messages insertObject:displayStr atIndex:0];
    [self.messages addObject:displayStr];
    [self.tView reloadData];
    NSIndexPath *topIndexPath = [NSIndexPath indexPathForRow:messages.count-1 inSection:0];
    [self.tView scrollToRowAtIndexPath:topIndexPath
                      atScrollPosition:UITableViewScrollPositionMiddle
                              animated:YES];
}


#pragma mark -
#pragma mark message delegate

-(void) onDownstreamMsgRecieved: (int) msgSize msgList : (NSArray*) msgList alarmIdx : (int) alarmIdx {
    NSLog(@"onMsgRecieved msg size = %d", msgSize);
    NSDictionary* msg;
    
    NSString* serverRegId;
    long serverTime;
    NSString* msgData;
    
    NSDateFormatter* format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
    
    for(int i = 0; i < msgSize; i++) {
        msg = msgList[i];
        // message sender(app server)
        serverRegId = msg[@"sender"];
        // message sent time
        serverTime = [msg[@"serverTime"] longValue];
        NSDate* date = [NSDate dateWithTimeIntervalSince1970:(serverTime /1000)];
        msgData = msg[@"data"];
        NSString* displayStr = [NSString stringWithFormat:@"%@  [sent time:%@]", msgData, [format stringFromDate:date]];
        //[self.messages insertObject:displayStr atIndex:0];
        [self.messages addObject:displayStr];
    }
    
    [self.tView reloadData];
    NSIndexPath *topIndexPath = [NSIndexPath indexPathForRow:messages.count-1 inSection:0];
    [self.tView scrollToRowAtIndexPath:topIndexPath
                      atScrollPosition:UITableViewScrollPositionMiddle
                              animated:YES];
}


-(void) onP2PMsgRecieved: (int) msgSize msgList : (NSArray*) msgList alarmIdx : (int) alarmIdx {
    NSLog(@"onP2PMsgRecieved msg size = %d", msgSize);
    NSDictionary* msg;
    NSString* sender; //sending device
    long serverTime;
    NSString* msgData;
    NSString* msgId;
    NSDateFormatter* format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
    
    for(int i = 0; i < msgSize; i++) {
        msg = msgList[i];
        msgId = msg[@"msgId"];
        sender = msg[@"sender"];
        serverTime = [msg[@"serverTime"] longValue];
        NSDate* date = [NSDate dateWithTimeIntervalSince1970:(serverTime /1000)];
        msgData = msg[@"data"];
        NSString* displayStr = [NSString stringWithFormat:@"p2p:%@  [sent time:%@]", msgData, [format stringFromDate:date]];
        //[self.messages insertObject:displayStr atIndex:0];
        [self.messages addObject:displayStr];
    }
    
    [self.tView reloadData];
    NSIndexPath *topIndexPath = [NSIndexPath indexPathForRow:messages.count-1 inSection:0];
    [self.tView scrollToRowAtIndexPath:topIndexPath
                      atScrollPosition:UITableViewScrollPositionMiddle
                              animated:YES];
}


-(void) onUpstreamMsgResult: (int) resultCode resultMsg : (NSString*) resultMsg umi : (NSString*) umi {
    NSLog(@"onUpstreamMsgResult umi = %@", umi);
}


-(void) onP2PMsgResult: (int) resultCode resultMsg : (NSString*) resultMsg pmi : (NSString*) pmi {
    NSLog(@"onP2PMsgResult pmi = %@", pmi);
}



#pragma mark -
#pragma mark Table delegates

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSString *s = (NSString *) [messages objectAtIndex:indexPath.row];
    static NSString *CellIdentifier = @"ChatCellIdentifier";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    cell.textLabel.text = s;
    return cell;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return messages.count;
}




@end

