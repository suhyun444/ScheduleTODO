let monthText = ['JAN.','FEB.','MAR.','APR.','MAY.','JUN.','JUL.','AGU.','SEP.','OCT.','NOV.','DEC.'];
let today = new Date();   
let currentCalenderStart;
let currentCalenderEnd;

monthCurrent();

const form = document.getElementById("input-form");

document.getElementById("input-save").addEventListener("click",function(){
    event.preventDefault();
    const formData = new FormData(form);
    const data = 
    {
        todo:
        {
            id : formData.get("id"),
            name : formData.get("name"),
            startDate : formData.get("startDate"),
            endDate : formData.get("endDate"),
            isCompleted : false
        },
        schedule:
        {
            color : formData.get("color"),
            description : formData.get("description")
        }
    }
    console.log(data);
    fetch("/save/schedule",
        {
            method: "POST",
            headers:{"Content-Type": "application/json"},
            body : JSON.stringify(data)
        }
    )
        .then(response => response.json())
        .then(schedule => {
            removeSchedule(formData.get("id"));
            createSchedule(schedule);
            closeAddSchedulePopup();
        });
});
document.getElementById("input-delete").addEventListener("click",function(){
    event.preventDefault(); 
    const formData = new FormData(form);
    fetch("/delete/schedule",
        {
            method: "POST",
            body : formData
        }
    )
        .then(response => {
            if(response.ok)
            {
                let id = formData.get("id");
                removeSchedule(id);
                closeAddSchedulePopup();
            }
        });
});
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
    currentCalenderStart = currentDate.toLocaleDateString("sv-SE");
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
    currentDate.setDate(currentDate.getDate() - 1);
    currentCalenderEnd = currentDate.toLocaleDateString("sv-SE");
    fetch('https://special-spork-p9px6j6vv6rcrjj6-8080.app.github.dev/get/schedules?start=' + currentCalenderStart +'&end='+currentCalenderEnd)
        .then(response => response.json())
        .then(data => createSchedules(data))
        .catch(error => console.error('erro', error));
    function addDayChild(iscurrent)
    {
        let day = document.createElement('div');
        day.classList.add('calender-date')
        if(!iscurrent)day.classList.add('calender-date-notcurrent');
        let dateInfo = currentDate.toLocaleDateString("sv-SE");
        day.addEventListener("click", function() {openAddSchedulePopup(null,null,"#00FFFF",dateInfo,dateInfo,null)});
        day.setAttribute('id',(currentDate.getMonth() + 1) + "-" + currentDate.getDate());
        day.innerText = (currentDate.getMonth() + 1 )+"/"+currentDate.getDate();
        currentDate.setDate(currentDate.getDate() + 1);
        dayContainer.appendChild(day);
    }
}
function createSchedules(schedules)
{
    schedules.forEach(schedule => {
        createSchedule(schedule);
    });
}
//https://bttrthn-ystrdy.tistory.com/19 appendChild는 복사해서 부모에게 넣어주는것이 아닌
//자식 객체를 부모객체에 넣어주는것이다 즉 복사가 아닌 이동이다
function createSchedule(schedule)
{
    let current = maxDate(new Date(currentCalenderStart),new Date(schedule.startDate));
    let last = minDate(new Date(currentCalenderEnd),new Date(schedule.endDate));
    while(current <= last)
    {    
        let day = document.getElementById((current.getMonth() + 1) + "-" + current.getDate());
        let scheduleButton = document.createElement('div');
        scheduleButton.innerText = schedule.name;
        scheduleButton.classList.add("schedule-"+schedule.id);
        scheduleButton.classList.add('calender-schedule');
        scheduleButton.style = 'background : '+schedule.color+';';
        scheduleButton.addEventListener("click", function() {event.stopPropagation();openAddSchedulePopup(schedule.id,schedule.name,schedule.color,schedule.startDate,schedule.endDate,schedule.description);});
        day.appendChild(scheduleButton); 
        current.setDate(current.getDate() + 1);
    }
}
function removeSchedule(id)
{
    console.log(id);
    document.querySelectorAll(".schedule-"+id).forEach(el => el.remove());
}
function minDate(a,b)
{
    if(a.getTime()< b.getTime())return a;
    return b;
}
function maxDate(a,b)
{
    if(a.getTime() > b.getTime())return a;
    return b;
}
function openAddSchedulePopup(id,name,color,startDate,endDate,description)
{
    if(id == null)
        document.getElementById("input-delete").style.visibility = "hidden";
    else
        document.getElementById("input-delete").style.visibility = "visible";
    let popup = document.getElementById('calender-addschedule-popup');
    let idInput = document.getElementById('input-id');
    let nameInput = document.getElementById('input-name');
    let colorInput = document.getElementById('input-color');
    let startDateInput = document.getElementById('input-start-date');
    let endDateInput = document.getElementById('input-end-date');
    let descriptionInput = document.getElementById('input-description');
    popup.style.display='flex';
    idInput.value = id;
    nameInput.value = name;
    colorInput.value = color;
    startDateInput.value = startDate;
    endDateInput.value = endDate;
    descriptionInput.value = description;

}
function closeAddSchedulePopup()
{
    let popup = document.getElementById('calender-addschedule-popup');
    popup.style.display='none';
}