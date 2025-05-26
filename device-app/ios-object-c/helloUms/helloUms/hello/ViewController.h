//
//  ViewController.h
//  hello
//
//  Created by Mac on 5/23/25.
//

#import <UIKit/UIKit.h>

#import <RationalOwl/MinervaDelegate.h>


@interface ViewController : UIViewController <DeviceRegisterResultDelegate, MessageDelegate>

@property (nonatomic, strong) NSString *mDeviceRegId;

@end
