a = -4;
b = -2;
c = 1;
L = (c - a) / 2;

N = 16;
t = a : 0.01 : c;
k = 1 : N;

a0 = -2.2;
ak = (4*pi*k .* sin(-8*pi*k/5) + 2*pi*k .* sin(2*pi*k/5)...
    + 5 * cos(2*pi*k/5) - 5 * cos(-4*pi*k/5)) ./ (2*pi^2*k.^2);
bk = (-4*pi*k .* cos(-8*pi*k/5) + 5 .* sin(2*pi*k/5)...
    - 2*pi*k .* cos(2*pi*k/5) + 5 * sin(-4*pi*k/5)) ./ (2*pi^2*k.^2);
harmonics = cos(pi/L*k'*t) .* repmat(ak',1,length(t))...
    + sin(pi/L*k'*t) .* repmat(bk',1,length(t));
Am = 1/pi./k;
s1 = harmonics .* repmat(Am', 1, length(t));
s2 = a0/2 + cumsum(s1);
for i = 1 : N
    subplot(4, 4, i)
    plot(t, s2(i,:))
end