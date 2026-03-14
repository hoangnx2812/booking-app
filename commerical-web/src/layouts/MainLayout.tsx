import { NavLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { isAuthenticated } from '../hooks/useAuth';
import styles from './MainLayout.module.css';

interface NavItemConfig {
  path: string;
  label: string;
  icon: (props: { active?: boolean }) => React.ReactNode;
  badge?: number;
  requireAuth?: boolean;
}

const NAV_ITEMS: NavItemConfig[] = [
  { path: '/', label: 'Home', icon: HomeIcon },
  { path: '/search', label: 'Search', icon: SearchIcon },
  { path: '/explore', label: 'Explore', icon: ExploreIcon },
  { path: '/reels', label: 'Reels', icon: ReelsIcon },
  { path: '/messages', label: 'Messages', icon: MessagesIcon, badge: 3 },
  { path: '/notifications', label: 'Notifications', icon: NotificationsIcon },
  { path: '/create', label: 'Create', icon: CreateIcon },
  { path: '/profile', label: 'Profile', icon: ProfileIcon },
];

const MOBILE_NAV: NavItemConfig[] = [
  { path: '/', label: 'Home', icon: HomeIcon },
  { path: '/search', label: 'Search', icon: SearchIcon },
  { path: '/explore', label: 'Explore', icon: ExploreIcon },
  { path: '/reels', label: 'Reels', icon: ReelsIcon },
  { path: '/profile', label: 'Profile', icon: ProfileIcon },
];

export default function MainLayout() {
  const location = useLocation();
  const navigate = useNavigate();
  const loggedIn = isAuthenticated();

  const isActive = (path: string) => {
    if (path === '/') return location.pathname === '/';
    return location.pathname.startsWith(path);
  };

  return (
    <div className={styles.layout}>
      <aside className={styles.sidebar}>
        <div className={styles.logo}>
          <NavLink to="/" className={styles.logoLink}>
            <span className={styles.logoText}>
              Beautygram
              <span className={styles.logoMini}>B</span>
            </span>
          </NavLink>
        </div>

        <nav className={styles.nav}>
          {NAV_ITEMS.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={isActive(item.path) ? styles.navItemActive : styles.navItem}
            >
              <span className={styles.navIcon}>
                <item.icon active={isActive(item.path)} />
              </span>
              <span className={styles.navLabel}>{item.label}</span>
              {item.badge && item.badge > 0 && (
                <span className={styles.navBadge}>{item.badge}</span>
              )}
            </NavLink>
          ))}
        </nav>

        <div className={styles.navBottom}>
          {loggedIn ? (
            <button
              className={styles.navItem}
              onClick={() => {
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                navigate('/login');
              }}
            >
              <span className={styles.navIcon}><LogoutIcon /></span>
              <span className={styles.navLabel}>Log out</span>
            </button>
          ) : (
            <NavLink to="/login" className={styles.navItem}>
              <span className={styles.navIcon}><LoginIcon /></span>
              <span className={styles.navLabel}>Log in</span>
            </NavLink>
          )}
          <button className={styles.navItem}>
            <span className={styles.navIcon}><MoreIcon /></span>
            <span className={styles.navLabel}>More</span>
          </button>
        </div>
      </aside>

      <main className={styles.content}>
        <Outlet />
      </main>

      <nav className={styles.bottomNav}>
        {MOBILE_NAV.map((item) => (
          <NavLink key={item.path} to={item.path} className={styles.bottomNavItem}>
            <item.icon active={isActive(item.path)} />
            {item.badge && item.badge > 0 && (
              <span className={styles.bottomNavBadge}>{item.badge}</span>
            )}
          </NavLink>
        ))}
      </nav>
    </div>
  );
}

/* ===== SVG Icons (Instagram style) ===== */

function HomeIcon({ active }: { active?: boolean }) {
  if (active) return (
    <svg aria-label="Home" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M22 23h-6.001a1 1 0 0 1-1-1v-5.455a2.997 2.997 0 1 0-5.993 0V22a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V11.543a1.002 1.002 0 0 1 .31-.724l10-9.543a1.001 1.001 0 0 1 1.38 0l10 9.543a1.002 1.002 0 0 1 .31.724V22a1 1 0 0 1-1 1Z" />
    </svg>
  );
  return (
    <svg aria-label="Home" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M9.005 16.545a2.997 2.997 0 0 1 2.997-2.997A2.997 2.997 0 0 1 15 16.545V22h7V11.543L12 2 2 11.543V22h7.005v-5.455Z" fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
}

function SearchIcon({ active }: { active?: boolean }) {
  return (
    <svg aria-label="Search" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M19 10.5A8.5 8.5 0 1 1 10.5 2a8.5 8.5 0 0 1 8.5 8.5Z" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth={active ? '3' : '2'} />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth={active ? '3' : '2'} x1="16.511" x2="22" y1="16.511" y2="22" />
    </svg>
  );
}

function ExploreIcon({ active }: { active?: boolean }) {
  if (active) return (
    <svg aria-label="Explore" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <polygon fillRule="evenodd" points="13.941 13.953 7.581 16.424 10.06 10.056 16.42 7.585 13.941 13.953" />
      <path d="M10.06 10.056 13.95 13.95" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
      <circle cx="12.001" cy="12.005" fill="none" r="10.5" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
  return (
    <svg aria-label="Explore" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <polygon fillRule="evenodd" points="13.941 13.953 7.581 16.424 10.06 10.056 16.42 7.585 13.941 13.953" fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" />
      <circle cx="12.001" cy="12.005" fill="none" r="10.5" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
}

function ReelsIcon({ active }: { active?: boolean }) {
  if (active) return (
    <svg aria-label="Reels" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="m12.823 1 2.974 5.002h-5.58l-2.65-4.971c.206-.013.419-.022.642-.027L8.55 1Zm2.327 0h.298c3.06 0 4.468.754 5.64 1.887a6.007 6.007 0 0 1 1.596 2.996l.07.378h-4.692L15.15 1ZM9.049 1.025l.093.002-.001.001 2.472 4.638H6.527c-.768 0-1.376.263-2.06.742-.08.056-.163.116-.256.185l-.15.113L1.86 3.97A8.013 8.013 0 0 1 5.347 1.58c.557-.248 1.145-.427 1.744-.515.332-.049.674-.04 1.958-.04ZM1.005 6.585 3.169 9.09c-.078.29-.128.586-.153.886a37.34 37.34 0 0 0-.012 3.587v.42l.003.189v.004c.087 3.102.759 4.663 2.088 6.05 1.329 1.388 2.89 2.06 5.994 2.148h.403l.168.001c1.22 0 2.194-.035 3.215-.13V13H1.005v-6.37l-.002-.045Zm4.024 12.216a.498.498 0 0 1 .262-.069h3.363a.498.498 0 0 1 0 .997H5.29a.498.498 0 0 1-.262-.928ZM15.002 22.27V13H23v3.516c0 3.06-.754 4.468-1.887 5.64-.967.999-2.16 1.565-4.129 1.862-.585.089-1.233.126-1.981.15v.102Zm-.003-9.27H23V6.585h-8.001v6.415Z" />
    </svg>
  );
  return (
    <svg aria-label="Reels" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <line fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" x1="2.049" x2="21.95" y1="7.002" y2="7.002" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="13.504" x2="16.362" y1="2.001" y2="7.002" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="7.207" x2="10.002" y1="2.11" y2="7.002" />
      <path d="M2 12.001v3.449c0 2.849.698 4.006 1.606 4.945.94.983 2.168 1.587 4.443 1.587h7.935c2.276 0 3.503-.604 4.444-1.587.908-.939 1.605-2.096 1.605-4.945V12" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
      <path d="M9.763 17.664a.908.908 0 0 1-.454-.787V11.63a.909.909 0 0 1 1.364-.788l4.631 2.6a.91.91 0 0 1 0 1.575l-4.63 2.6a.91.91 0 0 1-.911.047Z" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
      <path d="M2.019 7.002c-.012.14-.019.283-.019.428v4.571h19.95V7.43c0-.145-.007-.288-.02-.428H2.02Z" fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
}

function MessagesIcon({ active }: { active?: boolean }) {
  if (active) return (
    <svg aria-label="Messenger" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M12.003 1.131a10.487 10.487 0 0 0-10.87 10.497 10.49 10.49 0 0 0 3.412 7.771l.054 1.78a1.67 1.67 0 0 0 2.342 1.476l1.935-.872a11.25 11.25 0 0 0 3.127.444 10.487 10.487 0 0 0 10.87-10.497 10.487 10.487 0 0 0-10.87-10.6ZM8.6 14.565l-2.468-3.735a.7.7 0 0 1 .988-.988l2.984 1.965 2.352-2.985a.7.7 0 0 1 1.04-.139l2.468 3.735a.7.7 0 0 1-.988.988l-2.984-1.965-2.352 2.985a.7.7 0 0 1-1.04.14Z" />
    </svg>
  );
  return (
    <svg aria-label="Messenger" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M12.003 2.001a9.705 9.705 0 1 1 0 19.4 10.876 10.876 0 0 1-2.895-.384.798.798 0 0 0-.533.04l-1.984.876a.801.801 0 0 1-1.123-.708l-.054-1.78a.806.806 0 0 0-.27-.569 9.49 9.49 0 0 1-3.14-7.175 9.65 9.65 0 0 1 10-9.7Z" fill="none" stroke="currentColor" strokeMiterlimit="10" strokeWidth="1.739" />
      <path d="M17.79 10.132a.659.659 0 0 0-.962-.148l-2.332 2.187a.5.5 0 0 1-.6.01l-1.722-1.293a1.658 1.658 0 0 0-2.406.422l-2.537 3.63a.659.659 0 0 0 .962.149l2.332-2.188a.5.5 0 0 1 .6-.009l1.722 1.293a1.658 1.658 0 0 0 2.406-.422Z" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="1.2" />
    </svg>
  );
}

function NotificationsIcon({ active }: { active?: boolean }) {
  if (active) return (
    <svg aria-label="Notifications" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M16.792 3.904A4.989 4.989 0 0 1 21.5 9.122c0 3.072-2.652 4.959-5.197 7.222-2.512 2.243-3.865 3.469-4.303 3.752-.477-.309-2.143-1.823-4.303-3.752C5.141 14.072 2.5 12.167 2.5 9.122a4.989 4.989 0 0 1 4.708-5.218 4.21 4.21 0 0 1 3.675 1.941c.84 1.175.98 1.763 1.12 1.763s.278-.588 1.11-1.766a4.17 4.17 0 0 1 3.679-1.938Z" />
    </svg>
  );
  return (
    <svg aria-label="Notifications" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M16.792 3.904A4.989 4.989 0 0 1 21.5 9.122c0 3.072-2.652 4.959-5.197 7.222-2.512 2.243-3.865 3.469-4.303 3.752-.477-.309-2.143-1.823-4.303-3.752C5.141 14.072 2.5 12.167 2.5 9.122a4.989 4.989 0 0 1 4.708-5.218 4.21 4.21 0 0 1 3.675 1.941c.84 1.175.98 1.763 1.12 1.763s.278-.588 1.11-1.766a4.17 4.17 0 0 1 3.679-1.938Z" fill="none" stroke="currentColor" strokeWidth="2" />
    </svg>
  );
}

function CreateIcon({ active }: { active?: boolean }) {
  return (
    <svg aria-label="New post" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M2 12v3.45c0 2.849.698 4.005 1.606 4.944.94.984 2.168 1.588 4.443 1.588h7.935c2.275 0 3.504-.604 4.443-1.588C21.303 19.455 22 18.3 22 15.45V8.552c0-2.849-.697-4.006-1.573-4.944C19.456 2.627 18.228 2.02 15.953 2.02H8.049c-2.275 0-3.504.607-4.443 1.588C2.698 4.547 2 5.703 2 8.552Z" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth={active ? '2.5' : '2'} />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth={active ? '2.5' : '2'} x1="6.545" x2="17.455" y1="12.001" y2="12.001" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth={active ? '2.5' : '2'} x1="12.003" x2="12.003" y1="6.545" y2="17.455" />
    </svg>
  );
}

function ProfileIcon({ active }: { active?: boolean }) {
  return (
    <svg aria-label="Profile" height="24" viewBox="0 0 26 26" width="24">
      <circle cx="13" cy="7.25" fill="none" r="4" stroke="currentColor" strokeWidth={active ? '3' : '2'} />
      <path d="M6.26 23.75a9.01 9.01 0 0 1 13.48 0" fill="none" stroke="currentColor" strokeLinecap="round" strokeWidth={active ? '3' : '2'} />
      <circle cx="13" cy="13" fill="none" r="12" stroke="currentColor" strokeWidth={active ? '3' : '2'} />
    </svg>
  );
}

function LogoutIcon() {
  return (
    <svg height="24" viewBox="0 0 24 24" width="24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
      <polyline points="16,17 21,12 16,7" />
      <line x1="21" y1="12" x2="9" y2="12" />
    </svg>
  );
}

function LoginIcon() {
  return (
    <svg height="24" viewBox="0 0 24 24" width="24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4" />
      <polyline points="10,17 15,12 10,7" />
      <line x1="15" y1="12" x2="3" y2="12" />
    </svg>
  );
}

function MoreIcon() {
  return (
    <svg aria-label="Settings" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="3" x2="21" y1="4" y2="4" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="3" x2="21" y1="12" y2="12" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="3" x2="21" y1="20" y2="20" />
    </svg>
  );
}
