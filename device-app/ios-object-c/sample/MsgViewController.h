//
//  MsgViewController.h
//  sample
//
//  Created by 김정도 on 2018. 1. 22..
//  Copyright © 2018년 Rationalowl. All rights reserved.
//

#ifndef MsgViewController_h
#define MsgViewController_h


#import <UIKit/UIKit.h>
#include <RationalOwl/RationalOwl.h>


@interface MsgViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, MessageDelegate> {
    UIView            *msgListView;
    UITextField        *inputMessageField;
    UITableView        *tView;
    NSMutableArray    *messages;
    
    
}

@property (nonatomic, retain) IBOutlet UIView *msgListView;
@property (nonatomic, retain) IBOutlet UITextField    *inputMessageField;

@property (nonatomic, retain) IBOutlet UITableView    *tView;
@property (nonatomic, retain) NSMutableArray *messages;


- (IBAction)sendUpstreamMsg:(id)sender;

- (IBAction)sendP2PMsg:(id)sender;

@end


#endif /* MsgViewController_h */


