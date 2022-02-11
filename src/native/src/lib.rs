#![crate_type="cdylib"]

#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
#![allow(unused_variables)]

use std::borrow::{Borrow, BorrowMut};
use std::collections::HashSet;
use std::sync::Mutex;
use discord_game_sdk::{Discord, EventHandler};
use jni::sys::{jint, JNIEnv, jobject};
use lazy_static::lazy_static;

struct DiscordEvent {

}

impl EventHandler for DiscordEvent {

}

impl Default for DiscordEvent {
    fn default() -> Self {
        DiscordEvent{}
    }
}

lazy_static! {
    static ref DISCORDH: Mutex<Vec<Discord<'static, dyn EventHandler + Send>>> = Mutex::new(vec![]);
}

#[no_mangle]
pub extern fn Java_me_ddayo_discordmumble_client_discord_DiscordAPI_initialize(env: JNIEnv, object: jobject) {
    let mut d: Discord<DiscordEvent> = Discord::new(1).unwrap();
    *d.event_handler_mut() = Some(DiscordEvent::default());
    unsafe {
        DISCORDH.lock().unwrap().push(d);
    }
}

#[no_mangle]
pub extern fn Java_me_ddayo_discordmumble_client_discord_DiscordAPI_prcEvt(env: JNIEnv, object: jobject) {
    unsafe {
        DISCORDH.lock().unwrap().get(0);
    }
}
