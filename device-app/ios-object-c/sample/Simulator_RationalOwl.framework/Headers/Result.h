//
//  Result.h
//  ChatClient
//
//  Created by 김정도 on 2015. 7. 14..
//
//

#import <Foundation/Foundation.h>


@interface Result : NSObject {
    
}


/* result code which is related to common(device & app server)[1~ -99] */
extern int const RESULT_OK;
extern int const RESULT_UNKNOWN_ERROR;

/* CS(customer service) related */

//device registration related code
extern int const RESULT_DEVICE_NOT_YET_REGISTERED;
extern int const RESULT_DEVICE_ALREADY_REGISTERED;
extern int const RESULT_DEVICE_ID_NOT_MATCH;

//channel related code
extern int const RESULT_CHANNEL_ALREADY_CONNECTED;
extern int const RESULT_CHANNEL_IS_NOT_AVAILABLE;

extern int const RESULT_NETWORK_IS_NOT_AVAILABLE;
extern int const RESULT_INVALID_ARGUMENT;

//result messages
extern NSString* const RESULT_OK_MSG;
extern NSString* const RESULT_UNKNOWN_ERROR_MSG;

extern NSString* const RESULT_DEVICE_NOT_YET_REGISTERED_MSG;
extern NSString* const RESULT_DEVICE_ALREADY_REGISTERED_MSG;
extern NSString* const RESULT_DEVICE_ID_NOT_MATCH_MSG;

extern NSString* const RESULT_CHANNEL_ALREADY_CONNECTED_MSG;
extern NSString* const RESULT_CHANNEL_IS_NOT_AVAILABLE_MSG;

extern NSString* const RESULT_NETWORK_IS_NOT_AVAILABLE_MSG;
extern NSString* const RESULT_INVALID_ARGUMENT_MSG;

//class method
+ (NSString*) getResultMessage: (int) cId;


//instance method


@end
