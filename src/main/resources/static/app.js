const apiBase = '/api/public';

function getAuthToken(){ return localStorage.getItem('token'); }

async function fetchJSON(url, opts = {}){
  const headers = opts.headers || {};
  const token = getAuthToken();
  if(token) headers['Authorization'] = 'Bearer ' + token;
  const res = await fetch(url, {...opts, headers});
  if(!res.ok) throw new Error('Network error');
  return res.json();
}

async function init(){
  const produits = await fetchJSON(`${apiBase}/produits`);
  const sel = document.getElementById('produitSelect');
  produits.forEach(p=>{
    const opt = document.createElement('option'); opt.value=p.id; opt.textContent=p.nom; sel.appendChild(opt);
  });
}

document.getElementById('btnCompare').addEventListener('click', async ()=>{
  const produitId = document.getElementById('produitSelect').value;
  const date = document.getElementById('dateInput').value;
  if(!produitId||!date) return alert('Choisir produit et date');
  try{
    // show loader
    document.getElementById('loader').style.display = 'block';
    const [data, stats] = await Promise.all([
      fetchJSON(`${apiBase}/compare?produitId=${produitId}&date=${date}`),
      fetchJSON(`${apiBase}/stats?produitId=${produitId}&date=${date}`)
    ]);
    const tbody = document.querySelector('#prixTable tbody'); tbody.innerHTML='';
    // find cheapest
    let minPrice = null; let minIndex = -1;
    data.forEach((row,i)=>{ if(minPrice===null || Number(row.prix) < Number(minPrice)){ minPrice = row.prix; minIndex = i; } });
    data.forEach((row,i)=>{
      const tr=document.createElement('tr');
      const nm = row.marche? row.marche.nom : '—';
      const price = Number(row.prix);
      let indicatorClass = 'green';
      if(stats && stats.avg){
        const avg = Number(stats.avg);
        if(price >= avg * 1.10) indicatorClass = 'red';
        else if(price >= avg * 0.90) indicatorClass = 'yellow';
        else indicatorClass = 'green';
      }
      const badge = (i===minIndex) ? `<span class="badge">Marché le plus abordable</span>` : '';
      tr.innerHTML=`<td>${nm} ${badge}</td><td>${price}</td><td><span class="indicator ${indicatorClass}" aria-hidden="true"></span><span class="sr-only">${indicatorClass}</span></td>`;
      tbody.appendChild(tr);
    });

    // Summary
    const summary = document.getElementById('summary');
    if(stats){
      summary.innerHTML = `<strong>Min:</strong> ${stats.min || '-'} &nbsp; <strong>Moy:</strong> ${stats.avg || '-'} &nbsp; <strong>Max:</strong> ${stats.max || '-'}`;
    }

    // Chart - show price by market
    const labels = data.map(r=> r.marche? r.marche.nom: '');
    const values = data.map(r=> Number(r.prix));
    const ctx = document.getElementById('chart').getContext('2d');
    if(window._chart) window._chart.destroy();
    window._chart = new Chart(ctx, {type:'bar', data:{labels, datasets:[{label:'Prix', data:values, backgroundColor: labels.map((_,i)=> i===minIndex? '#A5D6A7':'#90caf9')}],}, options:{responsive:true, plugins:{legend:{display:false}}}});
    document.getElementById('loader').style.display = 'none';
  }catch(e){alert('Erreur: '+e.message)}
});

// small header token indicator
document.addEventListener('DOMContentLoaded', ()=>{
  const token = getAuthToken();
  if(token){
    const a = document.createElement('a'); a.href='#'; a.textContent='Déconnexion';
    a.addEventListener('click', ()=>{ localStorage.removeItem('token'); location.reload(); });
    document.querySelector('.banner .nav')?.appendChild(a);
  }
});

init().catch(e=>console.error(e));
