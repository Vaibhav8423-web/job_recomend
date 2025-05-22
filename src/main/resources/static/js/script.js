console.log("Script loaded");

let currentTheme = getTheme();

changeTheme();

function changeTheme(){
    //set to web page
    document.querySelector('html').classList.add(currentTheme);
    //set the listener to change theme button
    const changeThemeButton=document.querySelector('#theme_change_button');
    changeThemeButton.addEventListener("click",(event)=>{
        //remove the current theme
        document.querySelector('html').classList.remove(currentTheme);
        if(currentTheme=="dark"){
            //theme ko light krna hai
            currentTheme="light";
        }else{
            //theme ko dark krna hai
            currentTheme="dark";
        }
        //local storage mein update krenge
        setTheme(currentTheme);
        //set the current theme
        document.querySelector('html').classList.add(currentTheme);

        changeThemeButton.querySelector("span").textContent=currentTheme=="light"?"dark":"light";
    });
}

//set theme to local storage 
function setTheme(theme){
    localStorage.setItem("theme",theme);
}
//get theme from local storage
function getTheme(){
    let theme=localStorage.getItem("theme");
    if(theme) return theme;
    else return "light";
}