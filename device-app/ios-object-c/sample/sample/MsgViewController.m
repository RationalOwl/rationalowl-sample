//
//  MsgViewController.m
//  sample
//
//  Created by 김정도 on 2018. 1. 22..
//  Copyright © 2018년 Rationalowl. All rights reserved.
//

#import <RationalOwl/MinervaManager.h>

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
//regiser device

- (IBAction)sendUpstreamMsg:(id)sender {
    //NSString* svcId = @"d0a83353281e4b678774a0efa44fdd82";
    NSString* serverId = @"SVRf6c713ae-bbc6-4287-8c86-13bbd094ca41";
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
    [devices addObject:@"DVC2d2603be-b4d0-4dde-9094-0f10590279e2"];
    //[devices addObject:@"2f84c0dbc967493a8c401921f9191af4"];
    //[devices addObject:@"d5f1126b2eaf45dfbb9a545bb69fc8c4"];
    //[devices addObject:@"90aee24fcce741d6abcf4bf2dc6121c9"];
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


// display message to the table view.
-(void) displayToTable: msgList {
    NSDictionary* msg;
    NSString* serverRegId;
    long serverTime;
    NSObject* msgData;
    NSDateFormatter* format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"MM/dd HH:mm:ss"];
    
    int msgSize = (int)[msgList count];
    
    for(int i = 0; i < msgSize; i++) {
        msg = msgList[i];
        // message sender(app server)
        serverRegId = msg[@"sender"];
        // message sent time
        serverTime = [msg[@"serverTime"] longValue];
        NSDate* date = [NSDate dateWithTimeIntervalSince1970:(serverTime /1000)];
        msgData = msg[@"data"];
        
        /* this sample simply display msgdata to the table view */
        // msgData is simple string format or json string format(in custom push)
        NSString* displayStr = [NSString stringWithFormat:@"%@  [%@]", msgData, [format stringFromDate:date]];
        //[self.messages insertObject:displayStr atIndex:0];
        [self.messages addObject:displayStr];
    }
    
    [self.tView reloadData];
    NSIndexPath *topIndexPath = [NSIndexPath indexPathForRow:messages.count-1 inSection:0];
    [self.tView scrollToRowAtIndexPath:topIndexPath
                      atScrollPosition:UITableViewScrollPositionMiddle
                              animated:YES];
}

#pragma mark -
#pragma mark message delegate


-(void) onDownstreamMsgRecieved: (NSArray*) msgList {
    int msgSize = (int)[msgList count];
    NSLog(@"onDownstreamMsgRecieved msg size = %d", msgSize);
    
    // simply display message to the table view
    [self displayToTable: msgList];
}


-(void) onP2PMsgRecieved: (NSArray*) msgList {
    int msgSize = (int)[msgList count];
    NSLog(@"onP2PMsgRecieved msg size = %d", msgSize);
    
    // simply display message to the table view
    [self displayToTable: msgList];
}


-(void) onPushMsgRecieved: (int) msgSize msgList : (NSArray*) msgList alarmIdx : (int) alarmIdx {
    NSLog(@"onPushMsgRecieved msg size = %d", msgSize);
    
    // simply display message to the table view
    [self displayToTable: msgList];
}


-(void) onUpstreamMsgResult: (int) resultCode resultMsg : (NSString*) resultMsg msgId : (NSString*) msgId {
    NSLog(@"onUpstreamMsgResult msgId = %@", msgId);
}


-(void) onP2PMsgResult: (int) resultCode resultMsg : (NSString*) resultMsg msgId : (NSString*) msgId {
    NSLog(@"onP2PMsgResult msgId = %@", msgId);
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
    cell.textLabel.numberOfLines = 0;
    return cell;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return messages.count;
}




@end

