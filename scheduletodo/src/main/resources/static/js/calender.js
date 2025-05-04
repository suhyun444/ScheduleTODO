const monthText = ['JAN.','FEB.','MAR.','APR.','MAY.','JUN.','JUL.','AGU.','SEP.','OCT.','NOV.','DEC.'];
let today = new Date();   
let currentCalenderStart;
let currentCalenderEnd;
const todoListContainer = document.getElementById("todo-list-container"); 

monthCurrent();
initTodoList();
let todoDictionary = {};
class Todo
{
    constructor(id,name,startDate,endDate,isCompleted,color,description)
    {
        this.id = id;
        this.name = name;
        this.startDate =  startDate;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
        this.color = color;
        this.description = description;
    }
}

const scheduleForm = document.getElementById("input-form");
const todoForm = document.getElementById("input-todo-form");
document.getElementById('todo-add-button').addEventListener("click", function() {openAddTodoPopup(null,null,today.toLocaleDateString("sv-SE"),today.toLocaleDateString("sv-SE"))});
document.getElementById("input-save").addEventListener("click",()=>{
    event.preventDefault();
    const formData = new FormData(scheduleForm);
    const data = 
    {
        todo:
        {
            id : formData.get("id"),
            name : formData.get("name"),
            startDate : formData.get("startDate"),
            endDate : formData.get("endDate"),
            isCompleted : formData.get("isCompleted")
        },
        schedule:
        {
            color : formData.get("color"),
            description : formData.get("description")
        }
    }
    fetch("/save/schedule",
        {
            method: "POST",
            headers:{"Content-Type": "application/json"},
            body : JSON.stringify(data)
        }
    )
        .then(response => response.json())
        .then(schedule => {
            removeSchedule(data.todo.id);
            createSchedule(schedule);
            editTodo(data.todo.id,data.todo.name,data.todo.startDate,data.todo.endDate);
            closeAddSchedulePopup();
        });
});
document.getElementById("input-delete").addEventListener("click",()=>{
    event.preventDefault(); 
    const formData = new FormData(scheduleForm);
    const data = 
    {
        todo:
        {
            id : formData.get("id"),
            name : formData.get("name"),
            startDate : formData.get("startDate"),
            endDate : formData.get("endDate"),
            isCompleted : formData.get("isCompleted")
        },
        schedule:
        {
            color : formData.get("color"),
            description : formData.get("description")
        }
    }
    fetch("/delete/schedule",
        {
            method: "POST",
            headers:{"Content-Type": "application/json"},
            body : JSON.stringify(data)
        }
    )
        .then(response => {
            if(response.ok)
            {
                removeSchedule(data.todo.id);
                closeAddSchedulePopup();
            }
        });
});
document.getElementById("input-todo-save").addEventListener("click",()=>{
    event.preventDefault();
    const formData = new FormData(todoForm);
    const data = 
    {
        id : formData.get("id"),
        name : formData.get("name"),
        startDate : formData.get("startDate"),
        endDate : formData.get("endDate"),
        isCompleted : formData.get("isCompleted")
    }
    fetch("/save/todo",
        {
            method: "POST",
            headers:{"Content-Type": "application/json"},
            body : JSON.stringify(data)
        }
    )
        .then(response => response.json())
        .then(todo => {
            removeSchedule(data.id);
            scheduleInfo = 
            {
                id : todo.id,
                name : data.name,
                startDate : data.startDate,
                endData : data.endDate,
                isCompleted : todo.isCompleted,
                color : todo.color,
                description : todo.description
            }
            if(data.id == null)
            {
                createTodo(scheduleInfo);
                sortTodoList();
            }
            else 
            {
                if(todoDictionary[data.id].color != null)
                    createSchedule({id:data.id,name:data.name,startDate:data.startDate,endData:data.endDate,isCompleted:data.isCompleted,color:todoDictionary[data.id].color,description:todoDictionary[data.id].description});
                editTodo(data.id,data.name,data.startDate,data.endDate);
            }
            closeAddTodoPopup();
        });
            
});
document.getElementById("input-todo-delete").addEventListener("click",()=>{
    event.preventDefault(); 
    const formData = new FormData(todoForm);
    const data = 
    {
        id : formData.get("id"),
        name : formData.get("name"),
        startDate : formData.get("startDate"),
        endDate : formData.get("endDate"),
        isCompleted : formData.get("isCompleted")
    }
    fetch("/delete/todo",
        {
            method: "POST",
            headers:{"Content-Type": "application/json"},
            body : JSON.stringify(data)
        }
    )
        .then(response => {
            if(response.ok)
            {
                removeSchedule(data.id);
                document.getElementById("todo-"+data.id).remove();
                closeAddTodoPopup();
            }
        });
});
function initTodoList()
{
    fetch('https://special-spork-p9px6j6vv6rcrjj6-8080.app.github.dev/get/todolist?now=' + today.toLocaleDateString("sv-SE"))
    .then(response => response.json())
    .then(todoList =>
    {   
        todoList.forEach(data => 
        {
            createTodo(data);
        })
    }
    )
    .catch(error => console.error('erro', error));
}
function createTodo(data)
{
    if(!(data.id in todoDictionary))
    {
        let temp = new Todo(data.id,data.name,data.startDate,data.endDate,data.isCompleted,data.color,data.description);
        todoDictionary[data.id] = temp;
    }
    const tickForDay = 1000 * 60 * 60 * 24;
    let timeDiff = today - new Date(data.startDate);
    let dayDiff = Math.floor(timeDiff / tickForDay);
    let todo = document.createElement('div');

    let todoDayText = document.createElement('span');
    todoDayText.id = 'todo-'+data.id+'-daytext';
    todoDayText.classList.add('todo-day-text');
    if(dayDiff > 0 && new Date(data.endDate).getTime() + tickForDay < today.getTime())
    {
        timeDiff = today - new Date(data.endDate);
        dayDiff = Math.floor(timeDiff / tickForDay);
        todo.classList.add('todo-dayover');
        todoDayText.innerText = 'D+'+dayDiff;
    }
    else if(dayDiff  < 0)
    {
        todo.classList.add('todo-d'+Math.abs(dayDiff));
        todoDayText.innerText = 'D' + dayDiff;
    }
    else
    {
        todo.classList.add('todo-inprogress');
        todoDayText.innerText = 'D-Day';
    }

    let todoNameText = document.createElement('span');
    todoNameText.id = 'todo-'+data.id+'-nametext';
    todoNameText.classList.add('todo-name-text');
    todoNameText.innerText = data.name;

    let todoCheckBox = document.createElement('input');
    todoCheckBox.type = 'checkbox';
    todoCheckBox.checked = data.completed;
    todoCheckBox.addEventListener('click',()=>{event.stopPropagation();})
    todoCheckBox.addEventListener('change',()=>{updateTodo(data,todoCheckBox.checked);})
    todoCheckBox.classList.add('todo-checkbox');

    todo.appendChild(todoDayText);
    todo.appendChild(todoNameText);
    todo.appendChild(todoCheckBox)
    
    todo.id = 'todo-'+data.id;
    todo.classList.add("todo");
    todo.classList.add('calender-schedule');
    todo.addEventListener("click", function() {event.stopPropagation();openAddTodoPopup(todoDictionary[data.id].id,todoDictionary[data.id].name,todoDictionary[data.id].startDate,todoDictionary[data.id].endDate);});
    todoListContainer.appendChild(todo); 
    
        
}
function editTodo(id,name,startDate,endDate)
{
    const tickForDay = 1000 * 60 * 60 * 24;
    let timeDiff = today - new Date(startDate);
    let dayDiff = Math.floor(timeDiff / tickForDay);
    let todo = document.getElementById('todo-'+id);
    todo.className = '';

    let todoDayText = document.getElementById('todo-'+id+'-daytext');
    if(dayDiff > 0 && new Date(endDate).getTime() + tickForDay < today.getTime())
    {
        timeDiff = today - new Date(endDate);
        dayDiff = Math.floor(timeDiff / tickForDay);
        todo.classList.add('todo-dayover');
        todoDayText.innerText = 'D+'+dayDiff;
    }
    else if(dayDiff  < 0)
    {
        todo.classList.add('todo-d'+Math.abs(dayDiff));
        todoDayText.innerText = 'D' + dayDiff;
    }
    else
    {
        todo.classList.add('todo-inprogress');
        todoDayText.innerText = 'D-Day';
    }

    let todoNameText = document.getElementById('todo-'+id+'-nametext');
    todoNameText.innerText = name;
    todo.classList.add("todo");
    todo.classList.add('calender-schedule');
    sortTodoList();
}
function getAdjustedEndDate(startDate,endDate) {
    return (endDate < today) ? endDate : new Date(startDate.getTime() + 24 * 60 * 60 * 1000);
}
function sortTodoList()
{
    const todoElements = Array.from(todoListContainer.children).slice(1);
    todoElements.sort((a, b) => {
        let a_id = Number(a.id.substring(5));
        let b_id = Number(b.id.substring(5));
        const aDate = getAdjustedEndDate(new Date(todoDictionary[a_id].startDate),new Date(todoDictionary[a_id].endDate));
        const bDate = getAdjustedEndDate(new Date(todoDictionary[b_id].startDate),new Date(todoDictionary[b_id].endDate));
        return aDate - bDate;
    });
    todoElements.forEach(el => todoListContainer.appendChild(el));
}
function updateTodo(data,isCompleted)
{
    data.completed = isCompleted;
    let endDate = new Date(data.endDate);
    endDate.setDate(endDate.getDate() + 1);
    if(endDate < today)
    {
        document.getElementById("todo-"+data.id).remove();
    }
    fetch("/save/todo",
        {
            method: "POST",
            headers:{"Content-Type": "application/json"},
            body : JSON.stringify(data)
        }
    )
        .then(response => {
            if(!response.ok)
            {
                console.log("failed to save");
            }
        })
}
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
    let todo = new Todo(schedule.id,schedule.name,schedule.startDate,schedule.endDate,schedule.isCompleted,schedule.color,schedule.description);
    todoDictionary[schedule.id] = todo;
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
        scheduleButton.addEventListener("click", function() {event.stopPropagation();openAddSchedulePopup(todoDictionary[schedule.id].id,todoDictionary[schedule.id].name,todoDictionary[schedule.id].color,todoDictionary[schedule.id].startDate,todoDictionary[schedule.id].endDate,todoDictionary[schedule.id].description);});
        day.appendChild(scheduleButton); 
        current.setDate(current.getDate() + 1);
    }
}
function removeSchedule(id)
{
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
function openAddTodoPopup(id,name,startDate,endDate)
{
    if(id == null)
        document.getElementById("input-todo-delete").style.visibility = "hidden";
    else
        document.getElementById("input-todo-delete").style.visibility = "visible";
    let popup = document.getElementById('calender-addtodo-popup');
    let idInput = document.getElementById('input-todo-id');
    let nameInput = document.getElementById('input-todo-name');
    let startDateInput = document.getElementById('input-todo-start-date');
    let endDateInput = document.getElementById('input-todo-end-date');
    popup.style.display='flex';
    idInput.value = id;
    nameInput.value = name;
    startDateInput.value = startDate;
    endDateInput.value = endDate;
}
function closeAddTodoPopup()
{
    let popup = document.getElementById('calender-addtodo-popup');
    popup.style.display='none';
}