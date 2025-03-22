let monthText = ['JAN.','FEB.','MAR.','APR.','MAY.','JUN.','JUL.','AGU.','SEP.','OCT.','NOV.','DEC.'];
let today = new Date();   

monthCurrent();

function monthDecrease()
{
    if(currentMonth == 0 && currentYear == 1000) return;
    currentMonth--;
    if(currentMonth < 0)
    {
        currentMonth = 11;
        currentYear--;
    }
    updateText();
    moveCalender();
}
function monthIncrease()
{
    currentMonth++;
    if(currentMonth > 11)
    {
        currentMonth = 0;
        currentYear++;
    }
    updateText();
    moveCalender();
}
function monthCurrent()
{
    currentYear = today.getFullYear();
    currentMonth = today.getMonth(); 
    updateText();  
    moveCalender();
}
function updateText()
{
    let monthElement = document.getElementById('calender-info-month-text');
    monthElement.innerText = monthText[currentMonth];
    let yearElement = document.getElementById('calender-info-year-text');
    yearElement.innerText = currentYear;
}
function moveCalender()
{
    let dayContainer = document.getElementById("calender-date-container");
    dayContainer.replaceChildren();
    let currentDate = new Date(currentYear,currentMonth,1,0,0,0,0);
    let dayOfWeek = currentDate.getDay();
    currentDate.setDate(currentDate.getDate() - dayOfWeek);
    for(let i=0;i<dayOfWeek;++i)
    {
        addDayChild(false);
    }
    while(currentMonth == currentDate.getMonth())
    {
        addDayChild(true);
    }
    dayOfWeek = currentDate.getDay();
    for(let i=dayOfWeek;i<7;++i)
    {
        addDayChild(false);
    }
    function addDayChild(iscurrent)
    {
        let day = document.createElement('div');
        day.classList.add('calender-date')
        if(!iscurrent)day.classList.add('calender-date-notcurrent');
        let dateInfo = currentDate.toISOString().split("T")[0];
        day.onclick=function(){openAddSchedulePopup(dateInfo);}
        day.innerText = currentDate.getMonth()+"/"+currentDate.getDate();
        currentDate.setDate(currentDate.getDate() + 1);
        dayContainer.appendChild(day);
    }
}
function openAddSchedulePopup(date)
{
    let popup = document.getElementById('calender-addschedule-popup');
    popup.style.display='flex';
    let startDate = document.getElementById('input-start-date');
    let endDate = document.getElementById('input-end-date');
    startDate.value = date;
    endDate.value = date;
}
function closeAddSchedulePopup()
{
    let popup = document.getElementById('calender-addschedule-popup');
    popup.style.display='none';
}