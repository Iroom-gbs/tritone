#![crate_type="cdylib"]

#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
#![allow(unused_variables)]

use std::process::exit;
use discord_game_sdk::{Activity, Discord, EventHandler, SearchQuery};
use jni::{JavaVM, JNIEnv};
use jni::sys::{jboolean, JNI_FALSE, JNI_TRUE, jobject};

//ERROR_CODE
static NO_DISCORD: i32 = 1;
static ACTIVITY_UPDATE_FAILED: i32 = 2;
static LOBBY_CONNECT_FAILED: i32 = 3;
static RUN_CALLBACK_FAILED: i32 = 4;
static CHANGE_MUTE_FAILED: i32 = 5;
static INVALID_LOBBY: i32 = 6;
static JNI_ERROR: i32 = 7;

struct DiscordEvent {

}

impl EventHandler for DiscordEvent {

}

impl Default for DiscordEvent {
    fn default() -> Self {
        DiscordEvent{}
    }
}

static mut DISCORD : Option<Discord<DiscordEvent>> = Option::None;
static mut VM: Option<JavaVM> = Option::None;

fn getDiscord()-> &'static mut Discord<'static,DiscordEvent> {
    return unsafe { match DISCORD
    {
        Some(ref mut n) => n,
        None => exit(NO_DISCORD)
    }};
}

fn getVM()-> &'static mut JavaVM {
    return unsafe { match VM {
        Some(ref mut vm) => vm,
        None => exit(JNI_ERROR)
    }};
}

#[no_mangle]
pub extern fn Java_me_ddayo_discordmumble_client_discord_DiscordAPI_initialize(env: JNIEnv, object: jobject) {
    unsafe { VM = Some(env.get_java_vm().unwrap()); }
    match env.call_static_method("me/ddayo/discordmumble/client/discord/DiscordAPI", "nativeInitialized", "()V", &[]) {
        Err(e) => {
            println!("JNI ERROR");
            println!("{}", e);
        },
        Ok(r) => ()
    };
    unsafe { DISCORD = Some(Discord::new(941752061945581608).unwrap()); }
    getDiscord().update_activity(&Activity::empty()
        .with_state("Test")
        .with_details("와 성공!"), |discord, result| {
        println!("Callback received");
        let env = getVM().attach_current_thread_permanently().unwrap();
        if let Err(err) = result {
            exit(ACTIVITY_UPDATE_FAILED);
        }
    });
    println!("Setup finished: JNI");
}

#[no_mangle]
pub extern fn Java_me_ddayo_discordmumble_client_discord_DiscordAPI_tick(env: JNIEnv, object: jobject) {
    match getDiscord().run_callbacks() {
        Err(e) => exit(RUN_CALLBACK_FAILED),
        _ => ()
    }
}

#[no_mangle]
pub extern fn Java_me_ddayo_discordmumble_client_discord_DiscordAPI_setMute(env: JNIEnv, object: jobject) {
    match getDiscord().set_self_mute(true) {
        Err(e) => exit(CHANGE_MUTE_FAILED),
        _ => ()
    }
}

#[no_mangle]
pub extern fn Java_me_ddayo_discordmumble_client_discord_DiscordAPI_setUnmute(env: JNIEnv, object: jobject) {
    match getDiscord().set_self_mute(false) {
        Err(e) => exit(CHANGE_MUTE_FAILED),
        _ => ()
    }
}

#[no_mangle]
pub extern fn Java_me_ddayo_discordmumble_client_discord_DiscordAPI_isMuted(env: JNIEnv, object: jobject)->jboolean {

    return if getDiscord().self_muted().unwrap() { JNI_TRUE } else { JNI_FALSE };
}

#[no_mangle]
pub extern fn Java_me_ddayo_discordmumble_client_discord_DiscordAPI_getServerList(env: JNIEnv, object: jobject) {
    getDiscord().lobby_search(&SearchQuery::new(), |discord, result| {
        if let Err(err) = result {
            exit(LOBBY_CONNECT_FAILED);
        }
        let env = getVM().attach_current_thread_permanently().unwrap();
        println!("{}", discord.lobby_count());
        let serverList = match env.new_object_array(discord.lobby_count() as i32, env.find_class("/java/lang/String").unwrap(), env.new_string("").unwrap().into_inner()) {
            Err(e) => {
                println!("Array creation failed");
                panic!("Creation failed JNI")
                //exit(JNI_ERROR)
            },
            Ok(l) => l
        };
        let mut index = 0;
        for x in discord.iter_lobbies() {
            let lobby: i64 = x.unwrap();
            let name: String = match discord.lobby_metadata(lobby, "name") {
                Err(e) => exit(INVALID_LOBBY),
                Ok(s) => s
            };
            match env.set_object_array_element(serverList, index, env.new_string(lobby.to_string() + "/" + name.as_str()).unwrap()) {
                Err(e) => {
                    println!("String not valid");
                    //exit(JNI_ERROR)
                },
                Ok(r) => ()
            };
            index += 1;
        }
        match env.call_static_method("/me/ddayo/discordmumble/client/discord/DiscordAPI", "serverListReloaded", "([Ljava/lang/String;)V", &[serverList.into()]) {
            Err(e) => {
                println!("Method not found");
                //exit(JNI_ERROR)
            },
            Ok(r) => ()
        };
    });
}