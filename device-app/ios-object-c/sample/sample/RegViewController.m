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
    inputNameField.text = @"211.239.150.123"; // your gate server.
    
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
    //[mgr registerDevice:gateHost serviceId:@"afab0b12c8f44c00860195446032933d" deviceRegName:@"sampleIos"]; // cloud
    
    [mgr registerDevice:gateHost serviceId:@"SVC93197bc0-e186-4f27-a87d-1fcdc68b02c5" deviceRegName:@"sampleIos"]; //dev
   
}


- (IBAction) unregDevice {
    MinervaManager* mgr = [MinervaManager getInstance];
    [mgr unregisterDevice:@"SVC93197bc0-e186-4f27-a87d-1fcdc68b02c5"];
    
    //[mgr unregisterDevice:@"9bd4db31dbaa4897ad9aa81c3e7e183a"];
}


#pragma mark -
#pragma mark device register delegates


-(void) onRegisterResult: (int) resultCode resultMsg : (NSString*) resultMsg deviceRegId : (NSString*) deviceRegId {
    NSLog(@"onRegisterResult ");
    
    // device app registration success!
    // send deviceRegId to the app server.
    if(resultCode == 1) {
        MinervaManager* mgr = [MinervaManager getInstance];
        [mgr sendUpstreamMsg:@"your device app info including deviceRegId" serverRegId:@"your app server reg id here"];
    }
}


-(void) onUnregisterResult: (int) resultCode resultMsg : (NSString*) resultMsg {
    NSLog(@"onUnregisterResult ");
}


@end

