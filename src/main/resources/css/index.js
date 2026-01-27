/* Shared mock-data & helpers for pages */
const DATA = {
  items: [
    {id:1,name:'LED TV',cat:'Electronics',purchase:'2022-08-15',warranty:'2026-08-14',status:'Active'},
    {id:2,name:'Washing Machine',cat:'Appliances',purchase:'2021-03-10',warranty:'2025-03-10',status:'Expiring'},
    {id:3,name:'Microwave',cat:'Appliances',purchase:'2023-05-22',warranty:'2026-05-21',status:'Active'},
  ],
  documents: [
    {id:1, name:'TV_Invoice.pdf', item:'LED TV', size:'450KB'},
    {id:2, name:'WM_Warranty.pdf', item:'Washing Machine', size:'310KB'}
  ],
  reminders:[
    {id:1,title:'TV Warranty expires',date:'2026-08-01'},
    {id:2,title:'AC Filter Change',date:'2026-01-10'}
  ]
};

/* tiny router to set active sidebar link */
function setActive(page){
  document.querySelectorAll('.sidebar nav a').forEach(a=>{
    a.classList.toggle('active', a.dataset.page===page);
  });
}

/* INVENTORY: render table */
function renderInventoryTable(bodyId){
  const tbody = document.getElementById(bodyId);
  if(!tbody) return;
  tbody.innerHTML = '';
  DATA.items.forEach(it=>{
    const tr = document.createElement('tr');
    tr.innerHTML = `<td>${it.name}</td><td>${it.cat}</td><td>${it.purchase}</td><td>${it.warranty}</td><td>${it.status}</td>`;
    tbody.appendChild(tr);
  });
}

/* DOCUMENTS */
function renderDocs(listId){
  const ul = document.getElementById(listId);
  if(!ul) return;
  ul.innerHTML = '';
  DATA.documents.forEach(d=>{
    const li = document.createElement('li');
    li.innerHTML = `<strong>${d.name}</strong> · ${d.item} <span class="small" style="float:right">${d.size}</span>`;
    ul.appendChild(li);
  });
}

/* REMINDERS */
function renderReminders(listId){
  const ul = document.getElementById(listId);
  if(!ul) return;
  ul.innerHTML = '';
  DATA.reminders.forEach(r=>{
    const li = document.createElement('li');
    li.innerHTML = `<strong>${r.title}</strong><div class="small">${r.date}</div>`;
    ul.appendChild(li);
  });
}

/* ANALYTICS: create a chart in given canvas id */
function renderBarChart(canvasId,labels,values, label){
  const c = document.getElementById(canvasId);
  if(!c) return;
  new Chart(c, {
    type:'bar',
    data: { labels, datasets:[{ label: label||'Value', data: values, backgroundColor:'#2563eb'}] },
    options:{plugins:{legend:{display:false}}}
  });
}

/* AI chat (simple) */
function initChat(inputId,btnId,windowId){
  const input = document.getElementById(inputId), btn = document.getElementById(btnId), win = document.getElementById(windowId);
  if(!input||!btn||!win) return;
  btn.addEventListener('click', ()=>{
    const q = input.value.trim(); if(!q) return;
    const u = document.createElement('div'); u.className='chat-bubble user'; u.textContent = q; win.appendChild(u);
    input.value='';
    const b = document.createElement('div'); b.className='chat-bubble bot'; b.textContent='Thinking...'; win.appendChild(b); win.scrollTop = win.scrollHeight;
    setTimeout(()=>{ b.textContent = 'AI Suggestion: keep warranty docs, schedule service; check similar items in inventory.'; win.scrollTop = win.scrollHeight; }, 800);
  });
}

/* Modal open/close */
function openModal(id){document.getElementById(id).classList.add('open');}
function closeModal(id){document.getElementById(id).classList.remove('open');}

/* init per page */
document.addEventListener('DOMContentLoaded', ()=>{
  // attempt to render elements if present
  renderInventoryTable('invTableBody');
  renderDocs('docsList');
  renderReminders('remList');
  renderBarChart('analyticsChart',['Jan','Feb','Mar','Apr','May'],[1200,900,1500,800,1700],'Spending');
  initChat('aiInput','aiSend','aiWindow');

  // sidebar link clicks
  document.querySelectorAll('.sidebar nav a').forEach(a=>{
    a.addEventListener('click', ()=>{ const p=a.dataset.page; if(p) window.location.href = '/' + p + '.html'; });
  });
});
