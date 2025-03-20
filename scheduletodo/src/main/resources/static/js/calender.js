let monthText = ['JAN.','FEB.','MAR.','APR.','MAY.','JUN.','JUL.','AGU.','SEP.','OCT.','NOV.','DEC.'];
let today = new Date();   

monthCurrent();

function monthDecrease()
{
    if(currentMonth == 0 && currentYear == 1) return;
    currentMonth--;
    if(currentMonth < 0)
    {
        currentMonth = 11;
        currentYear--;
    }
    updateText();
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
}
function monthCurrent()
{
    currentYear = today.getFullYear();
    currentMonth = today.getMonth(); 
    updateText();  
}
function updateText()
{
    let monthElement = document.getElementById('calender-info-month-text');
    monthElement.innerText = monthText[currentMonth];
    let yearElement = document.getElementById('calender-info-year-text');
    yearElement.innerText = currentYear;
}