// Calculator JavaScript with Backend Integration
class Calculator {
    constructor() {
        this.currentInput = '0';
        this.previousInput = '';
        this.operation = '';
        this.shouldResetDisplay = false;
        this.isCalculating = false;
        
        this.display = document.getElementById('display');
        this.historyDisplay = document.getElementById('history');
        this.loadingSpinner = document.getElementById('loadingSpinner');
        this.errorModal = document.getElementById('errorModal');
        this.errorMessage = document.getElementById('errorMessage');
        this.statusDot = document.getElementById('statusDot');
        this.statusText = document.getElementById('statusText');
        
        this.initializeEventListeners();
        this.initializeParticles();
        this.updateDisplay();
    }
    
    initializeEventListeners() {
        // Theme toggle
        document.getElementById('themeToggle').addEventListener('click', this.toggleTheme);
        
        // Keyboard support
        document.addEventListener('keydown', (e) => this.handleKeyPress(e));
        
        // Close error modal when clicking outside
        this.errorModal.addEventListener('click', (e) => {
            if (e.target === this.errorModal) {
                this.closeErrorModal();
            }
        });
    }
    
    initializeParticles() {
        const particlesContainer = document.getElementById('particles');
        
        for (let i = 0; i < 50; i++) {
            const particle = document.createElement('div');
            particle.className = 'particle';
            particle.style.left = Math.random() * 100 + '%';
            particle.style.top = Math.random() * 100 + '%';
            particle.style.animationDelay = Math.random() * 20 + 's';
            particle.style.animationDuration = (Math.random() * 10 + 10) + 's';
            
            const keyframes = `
                @keyframes float${i} {
                    0%, 100% { transform: translate(0, 0) rotate(0deg); opacity: 0; }
                    10%, 90% { opacity: 1; }
                    25% { transform: translate(10px, -10px) rotate(90deg); }
                    50% { transform: translate(-5px, -20px) rotate(180deg); }
                    75% { transform: translate(-10px, -5px) rotate(270deg); }
                }
            `;
            
            const style = document.createElement('style');
            style.textContent = keyframes;
            document.head.appendChild(style);
            
            particle.style.animation = `float${i} ${particle.style.animationDuration} infinite linear`;
            particlesContainer.appendChild(particle);
        }
    }
    
    toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-theme');
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        
        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('calculator-theme', newTheme);
        
        // Update theme button icon
        const themeBtn = document.getElementById('themeToggle');
        const icon = themeBtn.querySelector('i');
        icon.className = newTheme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
        
        // Add bounce animation to calculator
        document.querySelector('.calculator-wrapper').classList.add('bounce-in');
        setTimeout(() => {
            document.querySelector('.calculator-wrapper').classList.remove('bounce-in');
        }, 500);
    }
    
    handleKeyPress(e) {
        const key = e.key;
        
        if (key >= '0' && key <= '9') {
            this.appendNumber(key);
        } else if (key === '.') {
            this.appendDecimal();
        } else if (key === '+') {
            this.setOperation('+');
        } else if (key === '-') {
            this.setOperation('-');
        } else if (key === '*') {
            this.setOperation('×');
        } else if (key === '/') {
            e.preventDefault(); // Prevent browser search
            this.setOperation('÷');
        } else if (key === 'Enter' || key === '=') {
            e.preventDefault();
            this.calculate();
        } else if (key === 'Escape') {
            this.clearAll();
        } else if (key === 'Backspace') {
            this.deleteLast();
        }
    }
    
    updateDisplay() {
        this.display.textContent = this.formatNumber(this.currentInput);
        
        if (this.operation && this.previousInput) {
            this.historyDisplay.textContent = `${this.formatNumber(this.previousInput)} ${this.operation}`;
        } else {
            this.historyDisplay.textContent = '';
        }
    }
    
    formatNumber(num) {
        if (num === '') return '0';
        if (num === '-') return '-';
        
        const number = parseFloat(num);
        if (isNaN(number)) return num;
        
        // Format large numbers with commas
        if (Math.abs(number) >= 1000) {
            return number.toLocaleString();
        }
        
        // Handle decimal places
        const str = number.toString();
        if (str.includes('.')) {
            const parts = str.split('.');
            if (parts[1].length > 8) {
                return number.toFixed(8);
            }
        }
        
        return str;
    }
    
    appendNumber(num) {
        if (this.shouldResetDisplay) {
            this.currentInput = '';
            this.shouldResetDisplay = false;
        }
        
        if (this.currentInput === '0' && num === '0') return;
        
        if (this.currentInput === '0' && num !== '.') {
            this.currentInput = num;
        } else {
            this.currentInput += num;
        }
        
        this.updateDisplay();
        this.addButtonPressAnimation();
    }
    
    appendDecimal() {
        if (this.shouldResetDisplay) {
            this.currentInput = '0';
            this.shouldResetDisplay = false;
        }
        
        if (this.currentInput.includes('.')) return;
        
        if (this.currentInput === '' || this.currentInput === '-') {
            this.currentInput += '0.';
        } else {
            this.currentInput += '.';
        }
        
        this.updateDisplay();
    }
    
    setOperation(op) {
        if (this.isCalculating) return;
        
        if (this.operation && this.previousInput && !this.shouldResetDisplay) {
            this.calculate();
        }
        
        this.operation = op;
        this.previousInput = this.currentInput;
        this.shouldResetDisplay = true;
        this.updateDisplay();
    }
    
    async calculate() {
        if (!this.operation || !this.previousInput || this.isCalculating) return;
        
        const a = parseFloat(this.previousInput);
        const b = parseFloat(this.currentInput);
        
        if (isNaN(a) || isNaN(b)) {
            this.showError('Invalid numbers provided');
            return;
        }
        
        this.showLoading(true);
        this.setStatus('calculating', 'Calculating...');
        this.display.classList.add('calculating');
        
        try {
            let endpoint;
            switch (this.operation) {
                case '+':
                    endpoint = '/api/calculator/add';
                    break;
                case '-':
                    endpoint = '/api/calculator/subtract';
                    break;
                case '×':
                    endpoint = '/api/calculator/multiply';
                    break;
                case '÷':
                    endpoint = '/api/calculator/divide';
                    break;
                default:
                    throw new Error('Invalid operation');
            }
            
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ a: a, b: b })
            });
            
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Calculation failed');
            }
            
            const result = await response.json();
            
            // Add success animation
            this.display.classList.add('success-glow');
            setTimeout(() => this.display.classList.remove('success-glow'), 1000);
            
            this.historyDisplay.textContent = `${this.formatNumber(a)} ${this.operation} ${this.formatNumber(b)} =`;
            this.currentInput = result.toString();
            this.operation = '';
            this.previousInput = '';
            this.shouldResetDisplay = true;
            this.updateDisplay();
            
            this.setStatus('success', 'Calculation complete');
            
        } catch (error) {
            console.error('Calculation error:', error);
            this.showError(error.message || 'An error occurred during calculation');
            this.setStatus('error', 'Calculation failed');
            
            // Add shake animation for errors
            document.querySelector('.calculator').classList.add('shake');
            setTimeout(() => document.querySelector('.calculator').classList.remove('shake'), 500);
            
        } finally {
            this.showLoading(false);
            this.display.classList.remove('calculating');
            this.isCalculating = false;
            
            // Reset status after 3 seconds
            setTimeout(() => this.setStatus('ready', 'Ready'), 3000);
        }
    }
    
    clearAll() {
        this.currentInput = '0';
        this.previousInput = '';
        this.operation = '';
        this.shouldResetDisplay = false;
        this.updateDisplay();
        this.setStatus('ready', 'Ready');
    }
    
    clearEntry() {
        this.currentInput = '0';
        this.updateDisplay();
    }
    
    deleteLast() {
        if (this.currentInput.length > 1) {
            this.currentInput = this.currentInput.slice(0, -1);
        } else {
            this.currentInput = '0';
        }
        this.updateDisplay();
    }
    
    showLoading(show) {
        this.isCalculating = show;
        if (show) {
            this.loadingSpinner.classList.add('show');
        } else {
            this.loadingSpinner.classList.remove('show');
        }
    }
    
    showError(message) {
        this.errorMessage.textContent = message;
        this.errorModal.classList.add('show');
    }
    
    closeErrorModal() {
        this.errorModal.classList.remove('show');
    }
    
    setStatus(type, message) {
        this.statusText.textContent = message;
        
        // Remove all status classes
        this.statusDot.classList.remove('status-ready', 'status-calculating', 'status-success', 'status-error');
        
        // Add appropriate status class
        this.statusDot.classList.add(`status-${type}`);
        
        // Update status dot color via CSS custom properties
        const colors = {
            ready: '#48bb78',
            calculating: '#ed8936',
            success: '#48bb78',
            error: '#e53e3e'
        };
        
        this.statusDot.style.background = colors[type] || colors.ready;
    }
    
    addButtonPressAnimation() {
        const display = document.querySelector('.display-current');
        display.style.transform = 'scale(1.02)';
        setTimeout(() => {
            display.style.transform = 'scale(1)';
        }, 100);
    }
}

// Global functions for button onclick handlers
let calculator;

function appendNumber(num) {
    calculator.appendNumber(num);
}

function appendDecimal() {
    calculator.appendDecimal();
}

function setOperation(op) {
    calculator.setOperation(op);
}

function calculate() {
    calculator.calculate();
}

function clearAll() {
    calculator.clearAll();
}

function clearEntry() {
    calculator.clearEntry();
}

function deleteLast() {
    calculator.deleteLast();
}

function closeErrorModal() {
    calculator.closeErrorModal();
}

// Initialize calculator when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    // Load saved theme
    const savedTheme = localStorage.getItem('calculator-theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);
    
    // Update theme button icon
    const themeBtn = document.getElementById('themeToggle');
    const icon = themeBtn.querySelector('i');
    icon.className = savedTheme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
    
    // Initialize calculator
    calculator = new Calculator();
    
    // Add entrance animation
    setTimeout(() => {
        document.querySelector('.calculator-wrapper').classList.add('bounce-in');
    }, 100);
});
