;(function(window,document,undefined){
var j=void 0,k=!0,l=null,m=!1;function n(a){return function(){return this[a]}}var o;function t(a,b,c){var d=2<arguments.length?Array.prototype.slice.call(arguments,2):[];return function(){d.push.apply(d,arguments);return b.apply(a,d)}};function aa(a,b){this.P=a;this.K=b||a;this.R=this.K.document;this.ba=j}aa.prototype.createElement=function(a,b,c){a=this.R.createElement(a);if(b)for(var d in b)if(b.hasOwnProperty(d))if("style"==d){var e=a,f=b[d];ba(this)?e.setAttribute("style",f):e.style.cssText=f}else a.setAttribute(d,b[d]);c&&a.appendChild(this.R.createTextNode(c));return a};function u(a,b,c){a=a.R.getElementsByTagName(b)[0];a||(a=document.documentElement);a&&a.lastChild&&a.insertBefore(c,a.lastChild)}
function ca(a){function b(){document.body?a():setTimeout(b,0)}b()}function v(a,b){return a.createElement("link",{rel:"stylesheet",href:b})}function da(a,b){return a.createElement("script",{src:b})}function w(a,b){for(var c=a.className.split(/\s+/),d=0,e=c.length;d<e;d++)if(c[d]==b)return;c.push(b);a.className=c.join(" ").replace(/\s+/g," ").replace(/^\s+|\s+$/,"")}
function x(a,b){for(var c=a.className.split(/\s+/),d=[],e=0,f=c.length;e<f;e++)c[e]!=b&&d.push(c[e]);a.className=d.join(" ").replace(/\s+/g," ").replace(/^\s+|\s+$/,"")}function ea(a,b){for(var c=a.className.split(/\s+/),d=0,e=c.length;d<e;d++)if(c[d]==b)return k;return m}function ba(a){if(a.ba===j){var b=a.R.createElement("p");b.innerHTML='<a style="top:1px;">w</a>';a.ba=/top/.test(b.getElementsByTagName("a")[0].getAttribute("style"))}return a.ba}
function y(a){var b=a.K.location.protocol;"about:"==b&&(b=a.P.location.protocol);return"https:"==b?"https:":"http:"};function z(a,b,c){this.Ta=a;this.Ra=b;this.Sa=c}z.prototype.J=n("Ta");z.prototype.Y=n("Ra");function A(a,b,c,d,e,f,g,i){this.Ja=a;this.Qa=b;this.wa=c;this.va=d;this.Na=e;this.Ma=f;this.ua=g;this.v=i}o=A.prototype;o.getName=n("Ja");o.Da=n("Qa");o.fa=n("wa");o.Aa=n("va");o.Ba=n("Na");o.Ca=n("Ma");o.za=n("ua");o.z=n("v");function B(a,b){this.a=a;this.q=b}var fa=new A("Unknown","Unknown","Unknown","Unknown","Unknown","Unknown",j,new z(m,m,m));
B.prototype.parse=function(){var a;if(-1!=this.a.indexOf("MSIE")){a=C(this);var b=D(this),c=E(this.a,/(MSIE [\d\w\.]+)/,1);if(""!=c){var d=c.split(" "),c=d[0],d=d[1],e=F(d),f=F(b);a=new A(c,d,c,d,a,b,G(this.q),new z("Windows"==a&&6<=e.e||"Windows Phone"==a&&8<=f.e,m,m))}else a=new A("MSIE","Unknown","MSIE","Unknown",a,b,G(this.q),new z(m,m,m))}else if(-1!=this.a.indexOf("Opera"))a=ga(this);else if(/AppleWeb(K|k)it/.test(this.a)){a=C(this);var b=D(this),c=E(this.a,/AppleWeb(?:K|k)it\/([\d\.\+]+)/,
1),g=m;""==c&&(c="Unknown");d=F(c);g=F(b);e="Unknown";-1!=this.a.indexOf("Chrome")||-1!=this.a.indexOf("CrMo")||-1!=this.a.indexOf("CriOS")?e="Chrome":"BlackBerry"==a||"Android"==a?e="BuiltinBrowser":-1!=this.a.indexOf("Safari")?e="Safari":-1!=this.a.indexOf("AdobeAIR")&&(e="AdobeAIR");f="Unknown";"BuiltinBrowser"==e?f="Unknown":-1!=this.a.indexOf("Version/")?f=E(this.a,/Version\/([\d\.\w]+)/,1):"Chrome"==e?f=E(this.a,/(Chrome|CrMo|CriOS)\/([\d\.]+)/,2):"AdobeAIR"==e&&(f=E(this.a,/AdobeAIR\/([\d\.]+)/,
1));"AdobeAIR"==e?(g=F(f),g=2<g.e||2==g.e&&5<=g.A):g="BlackBerry"==a?10<=g.e:"Android"==a?2<g.e||2==g.e&&1<g.A:526<=d.e||525<=d.e&&13<=d.A;a=new A(e,f,"AppleWebKit",c,a,b,G(this.q),new z(g,536>d.e||536==d.e&&11>d.A,"iPhone"==a||"iPad"==a||"iPod"==a||"Macintosh"==a))}else-1!=this.a.indexOf("Gecko")?(b=a="Unknown",c=m,-1!=this.a.indexOf("Firefox")?(a="Firefox",d=E(this.a,/Firefox\/([\d\w\.]+)/,1),""!=d&&(c=F(d),b=d,c=3<=c.e&&5<=c.A)):-1!=this.a.indexOf("Mozilla")&&(a="Mozilla"),d=E(this.a,/rv:([^\)]+)/,
1),""==d?d="Unknown":c||(c=F(d),c=1<c.e||1==c.e&&9<c.A||1==c.e&&9==c.A&&2<=c.La||d.match(/1\.9\.1b[123]/)!=l||d.match(/1\.9\.1\.[\d\.]+/)!=l),a=new A(a,b,"Gecko",d,C(this),D(this),G(this.q),new z(c,m,m))):a=fa;return a};function C(a){var b=E(a.a,/(iPod|iPad|iPhone|Android|Windows Phone|BB\d{2}|BlackBerry)/,1);if(""!=b)return/BB\d{2}/.test(b)&&(b="BlackBerry"),b;a=E(a.a,/(Linux|Mac_PowerPC|Macintosh|Windows|CrOS)/,1);return""!=a?("Mac_PowerPC"==a&&(a="Macintosh"),a):"Unknown"}
function D(a){var b=E(a.a,/(OS X|Windows NT|Android|CrOS) ([^;)]+)/,2);if(b||(b=E(a.a,/Windows Phone( OS)? ([^;)]+)/,2)))return b;if(b=E(a.a,/(iPhone )?OS ([\d_]+)/,2))return b;if(b=E(a.a,/Linux ([i\d]+)/,1))return b;return(a=E(a.a,/(BB\d{2}|BlackBerry).*?Version\/([^\s]*)/,2))?a:"Unknown"}
function ga(a){var b="Unknown",c="Unknown",d=E(a.a,/(Presto\/[\d\w\.]+)/,1);""!=d?(c=d.split("/"),b=c[0],c=c[1]):(-1!=a.a.indexOf("Gecko")&&(b="Gecko"),d=E(a.a,/rv:([^\)]+)/,1),""!=d&&(c=d));if(-1!=a.a.indexOf("Opera Mini/"))return d=E(a.a,/Opera Mini\/([\d\.]+)/,1),""==d&&(d="Unknown"),new A("OperaMini",d,b,c,C(a),D(a),G(a.q),new z(m,m,m));if(-1!=a.a.indexOf("Version/")){var e=E(a.a,/Version\/([\d\.]+)/,1);if(""!=e)return d=F(e),new A("Opera",e,b,c,C(a),D(a),G(a.q),new z(10<=d.e,m,m))}e=E(a.a,/Opera[\/ ]([\d\.]+)/,
1);return""!=e?(d=F(e),new A("Opera",e,b,c,C(a),D(a),G(a.q),new z(10<=d.e,m,m))):new A("Opera","Unknown",b,c,C(a),D(a),G(a.q),new z(m,m,m))}function F(a){var a=/([0-9]+)(?:\.([0-9]+)(?:\.([0-9]+)?)?)?/.exec(a),b={};a&&(b.e=parseInt(a[1]||-1,10),b.A=parseInt(a[2]||-1,10),b.La=parseInt(a[3]||-1,10));return b}function E(a,b,c){return(a=a.match(b))&&a[c]?a[c]:""}function G(a){if(a.documentMode)return a.documentMode};function ha(a,b,c){this.c=a;this.j=b;this.da=c;this.k="wf";this.h=new ja("-")}function ka(a){w(a.j,a.h.f(a.k,"loading"));H(a,"loading")}function I(a){x(a.j,a.h.f(a.k,"loading"));ea(a.j,a.h.f(a.k,"active"))||w(a.j,a.h.f(a.k,"inactive"));H(a,"inactive")}function H(a,b,c,d){if(a.da[b])a.da[b](c,d)};function la(){this.ma={}}function ma(a,b,c){var d=[],e;for(e in b)if(b.hasOwnProperty(e)){var f=a.ma[e];f&&d.push(f(b[e],c))}return d};function na(a,b){this.width=a;this.height=b};function J(a,b,c,d,e,f,g){this.c=b;this.D=c;this.t=d;this.C=e;this.O=f;this.X=0;this.qa=this.ka=m;this.ca=g;this.v=a.z()}
J.prototype.watch=function(a,b,c,d,e){var f=a.length;if(0===f)I(this.D);else{for(var g=0;g<f;g++){var i=a[g];b[i]||(b[i]=["n4"]);this.X+=b[i].length}e&&(this.ka=e);for(g=0;g<f;g++)for(var i=a[g],e=b[i],p=c[i],h=0,q=e.length;h<q;h++){var s=e[h],r=this.D,S=i,ia=s;w(r.j,r.h.f(r.k,S,ia,"loading"));H(r,"fontloading",S,ia);r=t(this,this.xa);S=t(this,this.ya);(new d(r,S,this.c,this.t,this.C,this.O,i,s,this.v,this.ca,l,p)).start()}}};
J.prototype.xa=function(a,b){var c=this.D;x(c.j,c.h.f(c.k,a,b,"loading"));x(c.j,c.h.f(c.k,a,b,"inactive"));w(c.j,c.h.f(c.k,a,b,"active"));H(c,"fontactive",a,b);this.qa=k;oa(this)};J.prototype.ya=function(a,b){var c=this.D;x(c.j,c.h.f(c.k,a,b,"loading"));ea(c.j,c.h.f(c.k,a,b,"active"))||w(c.j,c.h.f(c.k,a,b,"inactive"));H(c,"fontinactive",a,b);oa(this)};
function oa(a){0==--a.X&&a.ka&&(a.qa?(a=a.D,x(a.j,a.h.f(a.k,"loading")),x(a.j,a.h.f(a.k,"inactive")),w(a.j,a.h.f(a.k,"active")),H(a,"active")):I(a.D))};function K(a,b,c,d,e,f,g,i,p,h,q,s){this.Q=a;this.ha=b;this.c=c;this.t=d;this.C=e;this.O=f;this.M=g;this.s=i;this.H=s||"BESbswy";this.v=p;this.o={};this.ca=h||5E3;this.la=q||l;this.G=this.F=l;a=new L(this.c,this.t,this.H);M(a);for(var r in N)N.hasOwnProperty(r)&&(O(a,N[r],this.s),this.o[N[r]]=a.m());a.remove()}var N={Xa:"serif",Wa:"sans-serif",Va:"monospace",Ua:"Apple Color Emoji"};
K.prototype.start=function(){this.F=new L(this.c,this.t,this.H);M(this.F);this.G=new L(this.c,this.t,this.H);M(this.G);this.pa=this.O();O(this.F,this.M+",serif",this.s);O(this.G,this.M+",sans-serif",this.s);this.W()};function P(a,b,c){return a.v.Sa?!!a.o[c]&&b.width==a.o[c].width:!!a.o[c]&&b.width==a.o[c].width&&!!a.o[c]&&b.height==a.o[c].height}function pa(a,b,c){for(var d in N)if(N.hasOwnProperty(d)&&P(a,b,N[d])&&P(a,c,N[d]))return k;return m}
K.prototype.W=function(){var a=this.F.m(),b=this.G.m();P(this,a,"serif")&&P(this,b,"sans-serif")||this.v.Y()&&pa(this,a,b)?this.O()-this.pa>=this.ca?this.v.Y()&&pa(this,a,b)&&(this.la===l||this.la.hasOwnProperty(this.M))?Q(this,this.Q):Q(this,this.ha):qa(this):Q(this,this.Q)};function qa(a){a.C(function(a,c){return function(){c.call(a)}}(a,a.W),25)}function Q(a,b){a.F.remove();a.G.remove();b(a.M,a.s)};function L(a,b,c){this.c=a;this.t=b;this.H=c;this.Ia=new ra;this.I=new R;this.S=this.c.createElement("span",{},this.H)}function O(a,b,c){var d=a.c,e=a.S,c=c?a.I.expand(c):"",a="position:absolute;top:-999px;left:-999px;font-size:300px;width:auto;height:auto;line-height:normal;margin:0;padding:0;font-variant:normal;white-space:nowrap;font-family:"+a.Ia.quote(b)+";"+c;ba(d)?e.setAttribute("style",a):e.style.cssText=a}function M(a){u(a.c,"body",a.S)}L.prototype.m=function(){return this.t.m(this.S)};
L.prototype.remove=function(){var a=this.S;a.parentNode&&a.parentNode.removeChild(a)};function T(a,b,c,d){this.P=a;this.ea=b;this.C=c;this.a=d;this.Z=this.$=0}T.prototype.u=function(a,b){this.ea.ma[a]=b};T.prototype.load=function(a){var b=a.context||this.P;this.c=new aa(this.P,b);b=new ha(this.c,b.document.documentElement,a);this.a.z().J()?sa(this,b,a):I(b)};T.prototype.Ea=function(a,b,c,d){var e=a.ga?a.ga():K;d?a.load(t(this,this.Ka,b,c,e)):(a=0==--this.$,this.Z--,a&&(0==this.Z?I(b):ka(b)),c.watch([],{},{},e,a))};
T.prototype.Ka=function(a,b,c,d,e,f){var g=0==--this.$;g&&ka(a);this.C(t(this,function(a,b,c,d,e,f){a.watch(b,c||{},d||{},e,f)},b,d,e,f,c,g))};function sa(a,b,c){var d=ma(a.ea,c,a.c),c=c.timeout;a.Z=a.$=d.length;for(var c=new J(a.a,a.c,b,{m:function(a){return new na(a.offsetWidth,a.offsetHeight)}},a.C,function(){return(new Date).getTime()},c),e=0,f=d.length;e<f;e++){var g=d[e];g.L(a.a,t(a,a.Ea,g,b,c))}};function ja(a){this.Fa=a||"-"}ja.prototype.f=function(a){for(var b=[],c=0;c<arguments.length;c++)b.push(arguments[c].replace(/[\W_]+/g,"").toLowerCase());return b.join(this.Fa)};function ra(){this.oa="'"}ra.prototype.quote=function(a){for(var b=[],a=a.split(/,\s*/),c=0;c<a.length;c++){var d=a[c].replace(/['"]/g,"");-1==d.indexOf(" ")?b.push(d):b.push(this.oa+d+this.oa)}return b.join(",")};function R(){this.U=ta;this.p=ua}var ta=["font-style","font-weight"],ua={"font-style":[["n","normal"],["i","italic"],["o","oblique"]],"font-weight":[["1","100"],["2","200"],["3","300"],["4","400"],["5","500"],["6","600"],["7","700"],["8","800"],["9","900"],["4","normal"],["7","bold"]]};function U(a,b,c){this.ia=a;this.Oa=b;this.p=c}U.prototype.compact=function(a,b){for(var c=0;c<this.p.length;c++)if(b==this.p[c][1]){a[this.ia]=this.p[c][0];break}};
U.prototype.expand=function(a,b){for(var c=0;c<this.p.length;c++)if(b==this.p[c][0]){a[this.ia]=this.Oa+":"+this.p[c][1];break}};R.prototype.compact=function(a){for(var b=["n","4"],a=a.split(";"),c=0,d=a.length;c<d;c++){var e=a[c].replace(/\s+/g,"").split(":");if(2==e.length){var f=e[1];a:{for(var e=e[0],g=0;g<this.U.length;g++)if(e==this.U[g]){e=new U(g,e,this.p[e]);break a}e=l}e&&e.compact(b,f)}}return b.join("")};
R.prototype.expand=function(a){if(2!=a.length)return l;for(var b=[l,l],c=0,d=this.U.length;c<d;c++){var e=this.U[c];(new U(c,e,this.p[e])).expand(b,a.substr(c,1))}return b[0]&&b[1]?b.join(";")+";":l};var V=window.WebFont=function(){var a=(new B(navigator.userAgent,document)).parse();return new T(window,new la,function(a,c){setTimeout(a,c)},a)}();V.load=V.load;V.addModule=V.u;A.prototype.getName=A.prototype.getName;A.prototype.getVersion=A.prototype.Da;A.prototype.getEngine=A.prototype.fa;A.prototype.getEngineVersion=A.prototype.Aa;A.prototype.getPlatform=A.prototype.Ba;A.prototype.getPlatformVersion=A.prototype.Ca;A.prototype.getDocumentMode=A.prototype.za;A.prototype.getBrowserInfo=A.prototype.z;
z.prototype.hasWebFontSupport=z.prototype.J;z.prototype.hasWebKitFallbackBug=z.prototype.Y;function va(a,b){this.c=a;this.d=b}var wa={regular:"n4",bold:"n7",italic:"i4",bolditalic:"i7",r:"n4",b:"n7",i:"i4",bi:"i7"};va.prototype.L=function(a,b){return b(a.z().J())};
va.prototype.load=function(a){var b,c;u(this.c,"head",v(this.c,y(this.c)+"//webfonts.fontslive.com/css/"+this.d.key+".css"));var d=this.d.families,e,f;e=[];f={};for(var g=0,i=d.length;g<i;g++){c=c=b=j;c=d[g].split(":");b=c[0];if(c[1]){c=c[1].split(",");for(var p=[],h=0,q=c.length;h<q;h++){var s=c[h];if(s){var r=wa[s];p.push(r?r:s)}}c=p}else c=["n4"];e.push(b);f[b]=c}a(e,f)};V.u("ascender",function(a,b){return new va(b,a)});function W(a,b,c,d,e,f,g,i,p,h){W.Pa.call(this,a,b,c,d,e,f,g,i,p,h);a=["Times New Roman","Arial","Times","Sans","Serif"];b=a.length;c={};d=new L(this.c,this.t,this.H);M(d);O(d,a[0],this.s);c[d.m().width]=k;for(e=1;e<b;e++)f=a[e],O(d,f,this.s),c[d.m().width]=k,"4"!=this.s[1]&&(O(d,f,this.s[0]+"4"),c[d.m().width]=k);d.remove();this.B=c;this.ta=m;this.Ga=this.o.serif;this.Ha=this.o["sans-serif"]}(function(a,b){function c(){}c.prototype=a.prototype;b.prototype=new c;b.Pa=a;b.Ya=a.prototype})(K,W);
var xa={Arimo:k,Cousine:k,Tinos:k};W.prototype.W=function(){var a=this.F.m(),b=this.G.m();!this.ta&&a.width==b.width&&this.B[a.width]&&(this.B={},this.ta=this.B[a.width]=k);(this.Ga.width!=a.width||this.Ha.width!=b.width)&&!this.B[a.width]&&!this.B[b.width]?Q(this,this.Q):5E3<=this.O()-this.pa?this.B[a.width]&&this.B[b.width]&&xa[this.M]?Q(this,this.Q):Q(this,this.ha):qa(this)};function ya(a,b,c){this.V=a?a:b+za;this.g=[];this.aa=[];this.ra=c||""}var za="//fonts.googleapis.com/css";ya.prototype.f=function(){if(0==this.g.length)throw Error("No fonts to load !");if(-1!=this.V.indexOf("kit="))return this.V;for(var a=this.g.length,b=[],c=0;c<a;c++)b.push(this.g[c].replace(/ /g,"+"));a=this.V+"?family="+b.join("%7C");0<this.aa.length&&(a+="&subset="+this.aa.join(","));0<this.ra.length&&(a+="&text="+encodeURIComponent(this.ra));return a};function Aa(a){this.g=a;this.na=[];this.sa={};this.T={};this.I=new R}
var Ba={latin:"BESbswy",cyrillic:"&#1081;&#1103;&#1046;",greek:"&#945;&#946;&#931;",khmer:"&#x1780;&#x1781;&#x1782;",Hanuman:"&#x1780;&#x1781;&#x1782;"},Ca={thin:"1",extralight:"2","extra-light":"2",ultralight:"2","ultra-light":"2",light:"3",regular:"4",book:"4",medium:"5","semi-bold":"6",semibold:"6","demi-bold":"6",demibold:"6",bold:"7","extra-bold":"8",extrabold:"8","ultra-bold":"8",ultrabold:"8",black:"9",heavy:"9",l:"3",r:"4",b:"7"},Da={i:"i",italic:"i",n:"n",normal:"n"},Ea=RegExp("^(thin|(?:(?:extra|ultra)-?)?light|regular|book|medium|(?:(?:semi|demi|extra|ultra)-?)?bold|black|heavy|l|r|b|[1-9]00)?(n|i|normal|italic)?$");
Aa.prototype.parse=function(){for(var a=this.g.length,b=0;b<a;b++){var c=this.g[b].split(":"),d=c[0].replace(/\+/g," "),e=["n4"];if(2<=c.length){var f;var g=c[1];f=[];if(g)for(var g=g.split(","),i=g.length,p=0;p<i;p++){var h;h=g[p];if(h.match(/^[\w]+$/))if(h=Ea.exec(h.toLowerCase()),h==l)h="";else{var q=j;q=h[1];if(q==l)q="4";else var s=Ca[q],q=s?s:isNaN(q)?"4":q.substr(0,1);h=(h=this.I.expand([h[2]==l?"n":Da[h[2]],q].join("")))?this.I.compact(h):l}else h="";h&&f.push(h)}0<f.length&&(e=f);3==c.length&&
(c=c[2],f=[],c=!c?f:c.split(","),0<c.length&&(c=Ba[c[0]])&&(this.T[d]=c))}this.T[d]||(c=Ba[d])&&(this.T[d]=c);this.na.push(d);this.sa[d]=e}};function X(a,b,c){this.a=a;this.c=b;this.d=c}X.prototype.L=function(a,b){b(a.z().J())};X.prototype.ga=function(){return"AppleWebKit"==this.a.fa()?W:K};X.prototype.load=function(a){"MSIE"==this.a.getName()&&this.d.blocking!=k?ca(t(this,this.ja,a)):this.ja(a)};
X.prototype.ja=function(a){for(var b=this.c,c=new ya(this.d.api,y(b),this.d.text),d=this.d.families,e=d.length,f=0;f<e;f++){var g=d[f].split(":");3==g.length&&c.aa.push(g.pop());var i="";2==g.length&&""!=g[1]&&(i=":");c.g.push(g.join(i))}d=new Aa(d);d.parse();u(b,"head",v(b,c.f()));a(d.na,d.sa,d.T)};V.u("google",function(a,b){var c=(new B(navigator.userAgent,document)).parse();return new X(c,b,a)});function Y(a,b){this.c=a;this.d=b;this.g=[];this.w={};this.I=new R}Y.prototype.N=function(a){return y(this.c)+(this.d.api||"//f.fontdeck.com/s/css/js/")+(this.c.K.location.hostname||this.c.P.location.hostname)+"/"+a+".js"};
Y.prototype.L=function(a,b){var c=this.d.id,d=this.c.K,e=this;c?(d.__webfontfontdeckmodule__||(d.__webfontfontdeckmodule__={}),d.__webfontfontdeckmodule__[c]=function(a,c){for(var d=0,p=c.fonts.length;d<p;++d){var h=c.fonts[d];e.g.push(h.name);e.w[h.name]=[e.I.compact("font-weight:"+h.weight+";font-style:"+h.style)]}b(a)},u(this.c,"head",da(this.c,this.N(c)))):b(k)};Y.prototype.load=function(a){a(this.g,this.w)};V.u("fontdeck",function(a,b){return new Y(b,a)});function Z(a,b){this.c=a;this.d=b;this.g=[];this.w={}}Z.prototype.N=function(a){var b=y(this.c);return(this.d.api||b+"//use.typekit.net")+"/"+a+".js"};Z.prototype.L=function(a,b){var c=this.d.id,d=this.d,e=this.c.K,f=this;c?(e.__webfonttypekitmodule__||(e.__webfonttypekitmodule__={}),e.__webfonttypekitmodule__[c]=function(c){c(a,d,function(a,c,d){f.g=c;f.w=d;b(a)})},u(this.c,"head",da(this.c,this.N(c)))):b(k)};Z.prototype.load=function(a){a(this.g,this.w)};
V.u("typekit",function(a,b){return new Z(b,a)});function $(a,b,c){this.a=a;this.c=b;this.d=c;this.g=[];this.w={}}
$.prototype.L=function(a,b){var c=this,d=c.d.projectId,e=c.d.version;if(d){var f=c.c.createElement("script");f.id="__MonotypeAPIScript__"+d;var g=this.c.K,i=m;f.onload=f.onreadystatechange=function(){if(!i&&(!this.readyState||"loaded"===this.readyState||"complete"===this.readyState)){i=k;if(g["__mti_fntLst"+d]){var e=g["__mti_fntLst"+d]();if(e&&e.length){var h;for(h=0;h<e.length;h++)c.g.push(e[h].fontfamily)}}b(a.z().J());f.onload=f.onreadystatechange=l}};f.src=c.N(d,e);u(this.c,"head",f)}else b(k)};
$.prototype.N=function(a,b){var c=y(this.c),d=(this.d.api||"fast.fonts.com/jsapi").replace(/^.*http(s?):(\/\/)?/,"");return c+"//"+d+"/"+a+".js"+(b?"?v="+b:"")};$.prototype.load=function(a){a(this.g,this.w)};V.u("monotype",function(a,b){var c=(new B(navigator.userAgent,document)).parse();return new $(c,b,a)});function Fa(a,b){this.c=a;this.d=b}Fa.prototype.load=function(a){var b,c,d=this.d.urls||[],e=this.d.families||[];for(b=0,c=d.length;b<c;b++)u(this.c,"head",v(this.c,d[b]));var d=[],f={};for(b=0,c=e.length;b<c;b++){var g=e[b].split(":"),i=g[0],g=g[1];d.push(i);g&&(f[i]=(f[i]||[]).concat(g.split(",")))}a(d,f)};Fa.prototype.L=function(a,b){return b(a.z().J())};V.u("custom",function(a,b){return new Fa(b,a)});window.WebFontConfig&&V.load(window.WebFontConfig);
})(this,document);
