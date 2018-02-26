import * as express from 'express';
import * as bodyParser from "body-parser";
import * as logger from "morgan";
import { AppServerManager } from 'rational-js-server';

const app = express();

app.use(logger("dev"));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

const SERVICE_ID = 'b4a2166853ad4f19acc77b193d89c497';

app.post('/device-group', async (req, res) => {
    const name = req.body.name;
    const desc = req.body.desc;
    const devices = req.body.devices;
    try {
        const newGroup = await AppServerManager.getInstance().createDeviceGroup(name, desc, devices);
        res.send(newGroup).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});

app.post('/device-group/add/:groupId', async (req, res) => {
    const groupId = req.params.groupId;
    const devices = req.body.devices;
    try {
        const addGrpMsg = await AppServerManager.getInstance().addDeviceGroup(groupId, devices);
        res.send(addGrpMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});

app.post('/device-group/sub/:groupId', async (req, res) => {
    const groupId = req.params.groupId;
    const devices = req.body.devices;
    try {
        const subGrpMsg = await AppServerManager.getInstance().subtractDeviceGroup(groupId, devices);
        res.send(subGrpMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});

app.delete('/device-group/:groupId', async (req, res) => {
    const groupId = req.params.groupId;
    try {
        const deleteGrpMsg = await AppServerManager.getInstance().deleteDeviceGroup(groupId);
        res.send(deleteGrpMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});


app.post('/message/multi', async (req, res) => {
    const message = req.body.message;
    const devices = req.body.devices;
    try {
        const multicastMsg = await AppServerManager.getInstance().sendMulticastMsg(message, devices, false);
        res.send(multicastMsg).end();
    } catch (e) {
        console.log('error');
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});

app.post('/message/broad', async (req, res) => {
    const message = req.body.message;
    try {
        const broadcastMsg = await AppServerManager.getInstance().sendBroadcastMsg(message, false);
        res.send(broadcastMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});

app.post('/message/group', async (req, res) => {
    const message = req.body.message;
    const groupId = req.body.groupId;
    try {
        const groupMsg = await AppServerManager.getInstance().sendGroupMsg(message, groupId, false);
        res.send(groupMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
    }
});

app.listen(3000, async () => {
    await AppServerManager.getInstance()
        .registerAppServer(SERVICE_ID, 'sample-node-app-server', 'gate.rationalowl.com', 9081);
    console.log('Example app listening on port 3000!');
});