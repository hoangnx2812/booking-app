import { useState } from 'react';
import { useLogin } from '../../hooks/useAuth';
import styles from './LoginPage.module.css';

export default function LoginPage() {
  const loginMutation = useLogin();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  const canSubmit = username.length > 0 && password.length > 0;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (canSubmit) {
      loginMutation.mutate({ username, password });
    }
  };

  return (
    <div className={styles.wrapper}>
      <div className={styles.container}>
        <div className={styles.phonePreview}>
          <div className={styles.phoneFrame}>
            <div className={styles.phoneGradient} />
            <div className={styles.phoneText}>Beautygram</div>
            <div className={styles.phoneSubtext}>Discover & Book Beauty Services</div>
          </div>
        </div>

        <div className={styles.formSide}>
          <div className={styles.card}>
            <div className={styles.logo}>
              <div className={styles.logoText}>Beautygram</div>
            </div>

            <p className={styles.subtitle}>
              Sign in to see photos and videos from beauty artists
            </p>

            <form className={styles.form} onSubmit={handleSubmit}>
              <div className={styles.inputWrapper}>
                <input
                  className={styles.input}
                  type="text"
                  placeholder=" "
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  autoComplete="username"
                />
                <span className={styles.inputLabel}>Phone number, username, or email</span>
              </div>

              <div className={styles.inputWrapper}>
                <input
                  className={styles.input}
                  type={showPassword ? 'text' : 'password'}
                  placeholder=" "
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  autoComplete="current-password"
                />
                <span className={styles.inputLabel}>Password</span>
                {password.length > 0 && (
                  <button
                    type="button"
                    className={styles.showPasswordBtn}
                    onClick={() => setShowPassword(!showPassword)}
                  >
                    {showPassword ? 'Hide' : 'Show'}
                  </button>
                )}
              </div>

              <button
                type="submit"
                className={styles.loginBtn}
                disabled={!canSubmit || loginMutation.isPending}
              >
                {loginMutation.isPending ? 'Logging in...' : 'Log in'}
              </button>
            </form>

            <div className={styles.divider}>OR</div>

            <div className={styles.forgotLink}>
              <a>Forgot password?</a>
            </div>
          </div>

          <div className={styles.signupCard}>
            Don't have an account?{' '}
            <span className={styles.signupLink}>Sign up</span>
          </div>

          <div className={styles.getApp}>
            <div className={styles.getAppText}>Get the app.</div>
            <div className={styles.appBadges}>
              <div className={styles.appBadge}>App Store</div>
              <div className={styles.appBadge}>Google Play</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
