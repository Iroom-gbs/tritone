#![crate_type="cdylib"]

#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
#![allow(unused_variables)]

use std::borrow::Borrow;
use std::process::exit;
use discord_game_sdk::{Activity, Cast, Comparison, Discord, EventHandler, LobbyKind, LobbyMemberTransaction, LobbyTransaction, SearchQuery};
use jni::{JavaVM, JNIEnv};
use jni::objects::JString;
use jni::strings::JavaStr;
use jni::sys::{jboolean, jint, jlong, JNI_FALSE, JNI_TRUE, jobject};

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

const DISCORD_API_CLASS: &str = "me/ddayo/tritone/client/discord/DiscordAPI";

fn getDiscord()-> &'static mut Discord<'static,DiscordEvent> {
    return unsafe { match DISCORD
    {
        Some(ref mut n) => n,
        None => {
            println!("No Discord!!");
            exit(NO_DISCORD)
        }
    }};
}

fn getVM()-> &'static mut JavaVM {
    return unsafe { match VM {
        Some(ref mut vm) => vm,
        None => exit(JNI_ERROR)
    }};
}

#[no_mangle]
pub extern fn Java_me_ddayo_tritone_client_discord_DiscordAPI_initialize(env: JNIEnv, object: jobject) {
    unsafe { VM = Some(env.get_java_vm().unwrap()); }

    unsafe { DISCORD = Some(Discord::new(941752061945581608).unwrap()); }
    getDiscord().update_activity(&Activity::empty()
        .with_state("Test")
        .with_details("와 성공!"), |discord, result| {
        if let Err(err) = result {
            exit(ACTIVITY_UPDATE_FAILED);
        }
        let env = getVM().attach_current_thread_permanently().unwrap();
        env.call_static_method("me/ddayo/tritone/client/discord/DiscordAPI", "nativeInitialized", "()V", &[]);
    });
    println!("Setup finished: JNI");
}

#[no_mangle]
pub extern fn Java_me_ddayo_tritone_client_discord_DiscordAPI_tick(env: JNIEnv, object: jobject) {
    match getDiscord().run_callbacks() {
        Err(e) => exit(RUN_CALLBACK_FAILED),
        _ => ()
    }
}

#[no_mangle]
pub extern fn Java_me_ddayo_tritone_client_discord_DiscordAPI_setMute(env: JNIEnv, object: jobject) {
    match getDiscord().set_self_mute(true) {
        Err(e) => exit(CHANGE_MUTE_FAILED),
        _ => ()
    }
}

#[no_mangle]
pub extern fn Java_me_ddayo_tritone_client_discord_DiscordAPI_setUnmute(env: JNIEnv, object: jobject) {
    match getDiscord().set_self_mute(false) {
        Err(e) => exit(CHANGE_MUTE_FAILED),
        _ => ()
    }
}

#[no_mangle]
pub extern fn Java_me_ddayo_tritone_client_discord_DiscordAPI_isMuted(env: JNIEnv, object: jobject)->jboolean {

    return if getDiscord().self_muted().unwrap() { JNI_TRUE } else { JNI_FALSE };
}

fn get_mc_name(env: &JNIEnv)->String {
    //return "Testname".to_string();
    env.get_string(JString::from(env.call_static_method(DISCORD_API_CLASS, "getMCName", "()Ljava/lang/String;", &[]).unwrap().l().unwrap())).unwrap().to_str().unwrap().to_string()
}

#[no_mangle]
pub unsafe extern fn Java_me_ddayo_tritone_client_discord_DiscordAPI_joinLobby(env: JNIEnv, object: jobject, jlobbyName: JString) {
    let name = env.get_string(jlobbyName).unwrap().to_str().unwrap().to_string();
    println!("Start query");
    getDiscord().lobby_search(&SearchQuery::new()
        .filter("metadata.name".to_string(), Comparison::Equal, name.to_string(), Cast::String)
        .limit(10), move |discord, result| {

        if discord.lobby_count() == 0 {
            println!("Create!");
            discord.create_lobby(&LobbyTransaction::new()
                .capacity(10)
                .kind(LobbyKind::Public)
                .add_metadata("name".to_string(), name.to_string()), |discord, result| {
                let env = getVM().attach_current_thread_permanently().unwrap();
                env.call_static_method(DISCORD_API_CLASS, "lobbyMovedPre", "(J)V", &[(result.unwrap().id() as jlong).into()]);
                env.call_static_method(DISCORD_API_CLASS, "clearVoicePlayerList", "()V", &[]);

                discord.connect_lobby_voice(result.unwrap().id(), |discord, result| {
                    let env = getVM().attach_current_thread_permanently().unwrap();
                    env.call_static_method(DISCORD_API_CLASS, "voiceConnected", "()V", &[]);
                });

                discord.update_lobby(result.unwrap().id(), &LobbyTransaction::new()
                    .add_metadata("p".to_string(), result.unwrap().secret().to_string()), |discord, result| {
                    let env = getVM().attach_current_thread_permanently().unwrap();
                    env.call_static_method(DISCORD_API_CLASS, "lobbyMoved", "()V", &[]);
                });

                discord.update_member(result.unwrap().id(), discord.current_user().unwrap().id(), &LobbyMemberTransaction::new()
                    .add_metadata("mc".to_string(), get_mc_name(env.borrow())), |discord, result| {
                    match result {
                        Err(e) => {
                            println!("{}", e);
                            exit(JNI_ERROR);
                        }
                        _ => {}
                    }
                });
            })
        }
        else {
            discord.connect_lobby(discord.lobby_id_at(0).unwrap(), discord.lobby_metadata(discord.lobby_id_at(0).unwrap(), "p").unwrap(), |discord, result| {
                let env = getVM().attach_current_thread_permanently().unwrap();

                discord.update_member(result.unwrap().id(), discord.current_user().unwrap().id(), &LobbyMemberTransaction::new()
                    .add_metadata("mc".to_string(), get_mc_name(env.borrow())), |discord, result| {
                    match result {
                        Err(e) => {
                            println!("{}", e);
                            exit(JNI_ERROR);
                        }
                        _ => {}
                    }
                });

                let lid = result.unwrap().id();
                discord.connect_lobby_voice(result.unwrap().id(), move |discord, result| {
                    let env = getVM().attach_current_thread_permanently().unwrap();env.call_static_method(DISCORD_API_CLASS, "clearVoicePlayerList", "()V", &[]);
                    for user in discord.iter_lobby_member_ids(lid).unwrap() {
                        let mcName = discord.lobby_member_metadata(lid, user.unwrap(), "mc");
                        if mcName.is_ok() {
                            env.call_static_method(DISCORD_API_CLASS, "addVoicePlayer", "(Ljava/lang/String;J)V", &[env.new_string(mcName.unwrap()).unwrap().into(), (user.unwrap() as jlong).into()]);
                        }
                    }
                    env.call_static_method(DISCORD_API_CLASS, "voiceConnected", "()V", &[]);
                });

                env.call_static_method(DISCORD_API_CLASS, "lobbyMovedPre", "(J)V", &[(result.unwrap().id() as jlong).into()]);
                env.call_static_method(DISCORD_API_CLASS, "lobbyMoved", "()V", &[]);
            });
        }
    });
}

#[no_mangle]
pub extern fn Java_me_ddayo_tritone_client_discord_DiscordAPI_getServerList(env: JNIEnv, object: jobject) {
    getDiscord().lobby_search(&SearchQuery::new()
        .limit(10), |discord, result| {
        if let Err(err) = result {
            println!("{}", err);
            exit(LOBBY_CONNECT_FAILED);
        }
        let env = getVM().attach_current_thread_permanently().unwrap();
        let stringClass = env.find_class("java/lang/String").unwrap();
        let serverList = match env.new_object_array(discord.lobby_count() as i32, stringClass, env.new_string("").unwrap().into_inner()) {
            Err(e) => {
                println!("Array creation failed");
                exit(JNI_ERROR)
            },
            Ok(l) => l
        };
        let mut index = 0;
        println!("{}", discord.lobby_count());
        for x in discord.iter_lobbies() {
            let lobby: i64 = x.unwrap();
            let name: String = match discord.lobby_metadata(lobby, "name") {
                Err(e) => exit(INVALID_LOBBY),
                Ok(s) => s
            };
            println!("{} {}", lobby, name);
            match env.set_object_array_element(serverList, index, env.new_string(lobby.to_string() + "/" + name.as_str()).unwrap()) {
                Err(e) => {
                    println!("String not valid");
                    exit(JNI_ERROR)
                },
                Ok(r) => ()
            };
            index += 1;
        }
        match env.call_static_method(DISCORD_API_CLASS, "serverListReloaded", "([Ljava/lang/String;)V", &[serverList.into()]) {
            Err(e) => {
                println!("Method not found");
                exit(JNI_ERROR)
            },
            Ok(r) => ()
        };
    });
}

#[no_mangle]
pub extern fn Java_me_ddayo_tritone_client_discord_DiscordAPI_setVoiceLevel(env: JNIEnv, object: jobject, uid: jlong, level: jint) {
    println!("V: {} {}", uid, level as u8);
    match getDiscord().set_local_volume(uid, level as u8) {
        Err(e) => {
            println!("{}", e);
        }
        _ => ()
    }
}