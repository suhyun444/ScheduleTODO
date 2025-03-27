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
    let start = currentDate.toLocaleDateString("sv-SE");
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
    let end = currentDate.toLocaleDateString("sv-SE");
    fetch('https://special-spork-p9px6j6vv6rcrjj6-8080.app.github.dev/get/schedules?start=' + start +'&end='+end)
        .then(response => response.json())
        .then(data => createSchedules(data,new Date(start),new Date(end)))
        .catch(error => console.error('erro', error));
    function addDayChild(iscurrent)
    {
        let day = document.createElement('div');
        day.classList.add('calender-date')
        if(!iscurrent)day.classList.add('calender-date-notcurrent');
        let dateInfo = currentDate.toLocaleDateString("sv-SE");
        day.onclick=function(){openAddSchedulePopup(dateInfo);}
        day.setAttribute('id',(currentDate.getMonth() + 1) + "-" + currentDate.getDate());
        day.innerText = (currentDate.getMonth() + 1 )+"/"+currentDate.getDate();
        currentDate.setDate(currentDate.getDate() + 1);
        dayContainer.appendChild(day);
    }
}
//https://bttrthn-ystrdy.tistory.com/19 appendChild는 복사해서 부모에게 넣어주는것이 아닌
//자식 객체를 부모객체에 넣어주는것이다 즉 복사가 아닌 이동이다
function createSchedules(schedules,start,end)
{
    schedules.forEach(schedule => {
        let current = new Date(maxDate(start,schedule.startDate));
        let last = new Date(minDate(end,schedule.endDate));
        
        while(current <= last)
        {
            
            let day = document.getElementById((current.getMonth() + 1) + "-" + current.getDate());
            let scheduleButton = document.createElement('div');
            scheduleButton.setAttribute('id',schedule.id);
            scheduleButton.innerText = schedule.name;
            scheduleButton.classList.add('calender-schedule');
            scheduleButton.style = 'background : '+schedule.color+';';
            day.appendChild(scheduleButton); 
            current.setDate(current.getDate() + 1);
        }
    });
}
function minDate(a,b)
{
    if(a < b)return a;
    return b;
}
function maxDate(a,b)
{
    if(a > b)return a;
    return b;
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