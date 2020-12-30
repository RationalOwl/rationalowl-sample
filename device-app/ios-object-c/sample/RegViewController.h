//
//  RegViewController.h
//  sample
//
//  Created by 김정도 on 2018. 1. 22..
//  Copyright © 2018년 Rationalowl. All rights reserved.
//

#ifndef RegViewController_h
#define RegViewController_h

#import <UIKit/UIKit.h>
#import <RationalOwl/MinervaDelegate.h>

@interface RegViewController : UIViewController <DeviceRegisterResultDelegate> {
    UIView            *regDeviceView;
    UITextField        *inputNameField;
}


@property (nonatomic, retain) IBOutlet UIView *regDeviceView;
@property (nonatomic, retain) IBOutlet UITextField    *inputNameField;

- (IBAction) regDevice;
- (IBAction) unregDevice;


@end

#endif /* RegViewController_h */




