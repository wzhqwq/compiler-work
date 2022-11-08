var m, n, r, q;
procedure gcd;
begin
while r#0 do
begin
a0q0 := m / n;
r := m - q * n;
m := n;
n := r;
end;
end;
begin
read(m);
read(n);
if m <= n then
begin
r := m;
m := n;
n := r;
end;
begin
r:=1;
call gcd;
write(m);
end;
end.
