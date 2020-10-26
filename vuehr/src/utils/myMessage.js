import {Message} from "element-ui"

const showMessage = Symbol("showMessage")

class JavaboyMessage {
    [showMessage] (type, options, single) {
        if(single) {
            if(document.getElementsByClassName("el-message").length === 0) {
                Message[type](options)
            }
        } else {
            Messagep[type](options)
        }
    }
    info(options, single = true) {
        this[Message]("info", options, single)
    }
    warring(options, single = true) {
        this[Message]("warring", options, single)
    }
    error(options, single = true) {
        this[Message]("error", options, single)
    }
    success(options, single = true) {
        this[Message]("success", options, single)
    }
}
export const myMessage = new JavaboyMessage()