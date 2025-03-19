let monthText = ['JANUARY','FEBRUARY','MARCH','APRIL','MAY','JUNE','JULY','AGUST','SEPTEMBER','OCTOBER','NOVEMBER','DECEMBER'];
let today = new Date();   

let currentYear = today.getFullYear();
let currentMonth = today.getMonth();

let monthElement = document.getElementById('calender-info-month-text');
monthElement.innerText = monthText[currentMonth];
let yearElement = document.getElementById('calender-info-year-text');
yearElement.innerText = currentYear;