//
//  RegViewController.m
//  sample
//
//  Created by 김정도 on 2018. 1. 22..
//  Copyright © 2018년 Rationalowl. All rights reserved.
//

#import <RationalOwl/MinervaManager.h>
#import <RationalOwl/Result.h>

#import "RegViewController.h"

@interface RegViewController ()

@end

@implementation RegViewController

@synthesize regDeviceView;
@synthesize inputNameField;

#pragma mark -
#pragma mark IBActions
// view controller life cycle


- (void)viewDidLoad {
    [super viewDidLoad];
    //inputNameField.text = @"gate.rationalowl.com";
    inputNameField.text = @"3.39.200.245"; // your gate server.
    
    //set message delegate
    MinervaManager* minMgr = [MinervaManager getInstance];
    [minMgr setDeviceRegisterResultDelegate:self];
}


- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}


#pragma mark -
#pragma mark IBActions
//regiser device
- (IBAction) regDevice {
    
    NSString* gateHost = inputNameField.text;
    MinervaManager* mgr = [MinervaManager getInstance];
    [mgr registerDevice:gateHost serviceId:@"SVCea9fe858-a49e-4e73-a42a-55668f825e0b" deviceRegName:@"my i pad 11"];
    
    //[mgr registerDevice:gateHost serviceId:@"9bd4db31dbaa4897ad9aa81c3e7e183a" deviceRegName:@"my i pad 12"];
   
}


- (IBAction) unregDevice {
    MinervaManager* mgr = [MinervaManager getInstance];
    [mgr unregisterDevice:@"SVCea9fe858-a49e-4e73-a42a-55668f825e0b"];
    
    //[mgr unregisterDevice:@"9bd4db31dbaa4897ad9aa81c3e7e183a"];
}


#pragma mark -
#pragma mark device register delegates


-(void) onRegisterResult: (int) resultCode resultMsg : (NSString*) resultMsg deviceRegId : (NSString*) deviceRegId {
    NSLog(@"onRegisterResult ");
    
    // device app registration success!
    // send deviceRegId to the app server.
    if(resultCode == RESULT_OK) {
        MinervaManager* mgr = [MinervaManager getInstance];
        [mgr sendUpstreamMsg:@"your device app info including deviceRegId" serverRegId:@"your app server reg id here"];
    }
}


-(void) onUnregisterResult: (int) resultCode resultMsg : (NSString*) resultMsg {
    NSLog(@"onUnregisterResult ");
}


@end

