#import "UmsProtocol.h"
#import "PushAppProto.h"

#pragma mark - PushAppAuthNumberReq

@implementation PushAppAuthNumberReq

- (instancetype)init {
    _cId = APP_AUTH_NUMBER_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    _cId = [dict[@"cId"] intValue];
    _aId = dict[@"aId"] ?: @"";
    _cc = dict[@"cc"] ?: @"";
    _pn = dict[@"pn"] ?: @"";
    return self;
}

- (NSDictionary *)toDictionary {
    return @{ @"cId": @(_cId), @"aId": _aId ?: @"", @"cc": _cc ?: @"", @"pn": _pn ?: @"" };
}

@end

#pragma mark - PushAppAuthNumberRes

@implementation PushAppAuthNumberRes

- (instancetype)init {
    _cId = APP_AUTH_NUMBER_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    _cId = [dict[@"cId"] intValue];
    _aId = dict[@"aId"] ?: @"";
    _rc = [dict[@"rc"] intValue];
    _cmt = dict[@"cmt"];
    return self;
}

- (NSDictionary *)toDictionary {
    NSMutableDictionary *dict = [@{ @"cId": @(_cId), @"aId": _aId ?: @"", @"rc": @(_rc) } mutableCopy];
    if (_cmt != nil) dict[@"cmt"] = _cmt;
    return dict;
}

@end

#pragma mark - PushAppVerifyAuthNumberReq

@implementation PushAppVerifyAuthNumberReq

- (instancetype)init {
    _cId = APP_VERITY_AUTH_NUMBER_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    _cId = [dict[@"cId"] intValue];
    _aId = dict[@"aId"] ?: @"";
    _dt = [dict[@"dt"] intValue];
    _pn = dict[@"pn"] ?: @"";
    _an = dict[@"an"] ?: @"";
    return self;
}

- (NSDictionary *)toDictionary {
    return @{ @"cId": @(_cId), @"aId": _aId ?: @"", @"dt": @(_dt), @"pn": _pn ?: @"", @"an": _an ?: @"" };
}

@end

#pragma mark - PushAppVerifyAuthNumberRes

@implementation PushAppVerifyAuthNumberRes

- (instancetype)init {
    _cId = APP_VERITY_AUTH_NUMBER_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    _cId = [dict[@"cId"] intValue];
    _aId = dict[@"aId"] ?: @"";
    _rc = [dict[@"rc"] intValue];
    _cmt = dict[@"cmt"];
    return self;
}

- (NSDictionary *)toDictionary {
    NSMutableDictionary *dict = [@{ @"cId": @(_cId), @"aId": _aId ?: @"", @"rc": @(_rc) } mutableCopy];
    if (_cmt != nil) dict[@"cmt"] = _cmt;
    return dict;
}

@end

#pragma mark - PushAppInstallReq

@implementation PushAppInstallReq

- (instancetype)init {
    _cId = APP_INSTALL_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    _cId = [dict[@"cId"] intValue];
    _aId = dict[@"aId"] ?: @"";
    _dt = [dict[@"dt"] intValue];
    _dRId = dict[@"dRId"] ?: @"";
    _pn = dict[@"pn"];
    _auId = dict[@"auId"];
    _n = dict[@"n"];
    return self;
}

- (NSDictionary *)toDictionary {
    NSMutableDictionary *dict = [@{
        @"cId": @(_cId),
        @"aId": _aId ?: @"",
        @"dt": @(_dt),
        @"dRId": _dRId ?: @""
    } mutableCopy];
    if (_pn) dict[@"pn"] = _pn;
    if (_auId) dict[@"auId"] = _auId;
    if (_n) dict[@"n"] = _n;
    return dict;
}

@end

#pragma mark - PushAppInstallRes

@implementation PushAppInstallRes

- (instancetype)init {
    _cId = APP_INSTALL_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    _cId = [dict[@"cId"] intValue];
    _aId = dict[@"aId"] ?: @"";
    _rc = [dict[@"rc"] intValue];
    _cmt = dict[@"cmt"];
    _usRid = dict[@"usRid"] ?: @"";
    return self;
}

- (NSDictionary *)toDictionary {
    NSMutableDictionary *dict = [@{
        @"cId": @(_cId),
        @"aId": _aId ?: @"",
        @"rc": @(_rc),
        @"usRid": _usRid ?: @""
    } mutableCopy];
    if (_cmt) dict[@"cmt"] = _cmt;
    return dict;
}

@end


@implementation PushAppUnregUserReq

- (instancetype)init {
    self.cId = APP_UNREG_USER_CMD_ID;
    return self;
}

- (NSDictionary *)toDictionary {
    return @{
        @"cId": @(self.cId),
        @"aId": self.aId ?: @"",
        @"dRId": self.dRId ?: @""
    };
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    PushAppUnregUserReq *obj = [PushAppUnregUserReq new];
    obj.cId = [dict[@"cId"] intValue];
    obj.aId = dict[@"aId"];
    obj.dRId = dict[@"dRId"];
    return obj;
}

@end


@implementation PushAppUnregUserRes

- (instancetype)init {
    self.cId = APP_UNREG_USER_CMD_ID;
    return self;
}

- (NSDictionary *)toDictionary {
    NSMutableDictionary *json = [@{
        @"cId": @(self.cId),
        @"aId": self.aId ?: @"",
        @"rc": @(self.rc),
        @"dRId": self.dRId ?: @""
    } mutableCopy];
    if (self.cmt != nil) {
        json[@"cmt"] = self.cmt;
    }
    return json;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    PushAppUnregUserRes *obj = [PushAppUnregUserRes new];
    obj.cId = [dict[@"cId"] intValue];
    obj.aId = dict[@"aId"];
    obj.rc = [dict[@"rc"] intValue];
    obj.cmt = dict[@"cmt"];
    obj.dRId = dict[@"dRId"];
    return obj;
}

@end



@implementation PushAppMsgReadNoti

- (instancetype)init {
    self = [super init];
    self.cId = APP_MSG_READ_NOTI_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    self = [super init];
    self.cId = [dict[@"cId"] intValue];
    self.aId = dict[@"aId"];
    self.dRId = dict[@"dRId"];
    self.mId = dict[@"mId"];
    return self;
}

- (NSDictionary *)toDictionary {
    return @{
        @"cId": @(self.cId),
        @"aId": self.aId,
        @"dRId": self.dRId,
        @"mId": self.mId
    };
}

@end


@implementation PushAppMsgInfoReq

- (instancetype)init {
    self = [super init];
    self.cId = APP_MSG_INFO_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    self = [super init];
    self.cId = [dict[@"cId"] intValue];
    self.aId = dict[@"aId"];
    self.dRId = dict[@"dRId"];
    self.mId = dict[@"mId"];
    return self;
}

- (NSDictionary *)toDictionary {
    return @{
        @"cId": @(self.cId),
        @"aId": self.aId,
        @"dRId": self.dRId,
        @"mId": self.mId
    };
}

@end


@implementation PushAppMsgInfoRes

- (instancetype)init {
    self = [super init];
    self.cId = APP_MSG_INFO_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    self = [super init];
    self.cId = [dict[@"cId"] intValue];
    self.aId = dict[@"aId"];
    self.rc = [dict[@"rc"] intValue];
    self.cmt = dict[@"cmt"];
    self.mId = dict[@"mId"];
    self.ast = [dict[@"ast"] longLongValue];
    self.ase = [dict[@"ase"] intValue];
    self.mst = [dict[@"mst"] longLongValue];
    self.mt = [dict[@"mt"] intValue];
    self.ms = [dict[@"ms"] intValue];
    return self;
}

- (NSDictionary *)toDictionary {
    NSMutableDictionary *dict = [@{
        @"cId": @(self.cId),
        @"aId": self.aId,
        @"rc": @(self.rc),
        @"mId": self.mId,
        @"ast": @(self.ast),
        @"ase": @(self.ase),
        @"mst": @(self.mst),
        @"mt": @(self.mt),
        @"ms": @(self.ms)
    } mutableCopy];

    if (self.cmt != nil) {
        dict[@"cmt"] = self.cmt;
    }

    return dict;
}

@end


@implementation PushAppImgDataReq

- (instancetype)init {
    self = [super init];
    self.cId = APP_IMG_DATA_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    self = [super init];
    self.cId = [dict[@"cId"] intValue];
    self.aId = dict[@"aId"];
    self.mId = dict[@"mId"];
    self.iId = dict[@"iId"];
    return self;
}

- (NSDictionary *)toDictionary {
    return @{
        @"cId": @(self.cId),
        @"aId": self.aId,
        @"mId": self.mId,
        @"iId": self.iId
    };
}

@end


@implementation PushAppImgDataRes

- (instancetype)init {
    self = [super init];
    self.cId = APP_IMG_DATA_CMD_ID;
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    self = [super init];
    self.cId = [dict[@"cId"] intValue];
    self.aId = dict[@"aId"];
    self.rc = [dict[@"rc"] intValue];
    self.cmt = dict[@"cmt"];
    self.imgD = dict[@"imgD"];
    return self;
}

- (NSDictionary *)toDictionary {
    NSMutableDictionary *dict = [@{
        @"cId": @(self.cId),
        @"aId": self.aId,
        @"rc": @(self.rc),
        @"imgD": self.imgD
    } mutableCopy];

    if (self.cmt != nil) {
        dict[@"cmt"] = self.cmt;
    }

    return dict;
}

@end
